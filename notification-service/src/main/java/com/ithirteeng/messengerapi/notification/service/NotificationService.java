package com.ithirteeng.messengerapi.notification.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.dto.*;
import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import com.ithirteeng.messengerapi.notification.mapper.NotificationMapper;
import com.ithirteeng.messengerapi.notification.repository.NotificationRepository;
import com.ithirteeng.messengerapi.notification.utils.NotificationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(CreateNotificationDto dto) {
        var entity = NotificationMapper.entityFromCreateDto(dto);
        notificationRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public Integer countNotReadNotifications(UUID userId) {
        return notificationRepository.countByUserIdAndStatus(userId, NotificationStatus.NOT_READ);
    }

    @Transactional
    public Integer setStatusToNotifications(UpdateStatusDto updateStatusDto, UUID userId) {
        List<UUID> invalidIds = new ArrayList<>();
        for (UUID id : updateStatusDto.getNotificationsList()) {
            if (!notificationRepository.existsAllByIdAndUserId(id, userId)) {
                invalidIds.add(id);
            }
        }

        if (!invalidIds.isEmpty()) {
            throw new NotFoundException("Такие уведомления у пользователя отсутсвтуют: " + invalidIds);
        }

        LocalDateTime time = LocalDateTime.now();
        if (updateStatusDto.getStatus() == NotificationStatus.NOT_READ) {
            time = null;
        }

        notificationRepository.changeStatusOfNotifications(
                updateStatusDto.getNotificationsList(),
                updateStatusDto.getStatus(),
                time
        );

        return countNotReadNotifications(userId);
    }

    @Transactional
    public NotificationsPageListDto getNotifications(PageFiltersDto pageFiltersDto, UUID userId) {
        int pageNumber = getPageNumber(pageFiltersDto);
        int pageSize = getPageSize(pageFiltersDto);
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "receiveTime")
        );

        Specification<NotificationEntity> specification = setupSpecification(pageFiltersDto.getFilters(), userId);

        Page<NotificationEntity> notificationsPage = notificationRepository.findAll(specification, pageable);

        List<NotificationDto> resultList = new ArrayList<>();
        for (NotificationEntity entity : notificationsPage.getContent()) {
            resultList.add(NotificationMapper.dtoFromEntity(entity));
        }

        NotificationsPageListDto result = new NotificationsPageListDto();
        result.setNotificationsList(resultList);
        result.setPageInfo(new PageInfoDto(pageNumber, pageSize));

        return result;
    }

    private Specification<NotificationEntity> setupSpecification(NotificationsFiltersDto filters, UUID userId) {
        Specification<NotificationEntity> specification = Specification.where(NotificationSpecification.userIdEqual(userId));
        if (filters != null) {
            if (filters.getPeriodFilter() != null) {
                if (filters.getPeriodFilter().getFromDateTime() != null) {
                    specification = specification.and(
                            NotificationSpecification.greaterReceivedDate(filters.getPeriodFilter().getFromDateTime())
                    );
                }
                if (filters.getPeriodFilter().getToDateTime() != null) {
                    specification = specification.and(
                            NotificationSpecification.lessReceivedDate(filters.getPeriodFilter().getToDateTime())
                    );
                }
            }
            if (filters.getTextFilter() != null) {
                specification = specification.and(NotificationSpecification.textLike(filters.getTextFilter()));
            }
            if (filters.getTypes() != null && !filters.getTypes().isEmpty()) {
                specification = specification.and(NotificationSpecification.typeIn(filters.getTypes()));
            }
        }
        return specification;
    }

    private int getPageNumber(PageFiltersDto pageFiltersDto) {
        int pageNumber = 1;
        if (pageFiltersDto.getPageInfo().getPageNumber() != null) {
            pageNumber = pageFiltersDto.getPageInfo().getPageNumber();
            if (pageNumber <= 0) {
                throw new BadRequestException("Размер страницы должен быть больше 0");
            }
        }
        return pageNumber;
    }

    private int getPageSize(PageFiltersDto pageFiltersDto) {
        int pageSize = 10;
        if (pageFiltersDto.getPageInfo().getPageSize() != null) {
            pageSize = pageFiltersDto.getPageInfo().getPageSize();
            if (pageSize <= 0) {
                throw new BadRequestException("Размер страницы должен быть больше 0");
            }
        }
        return pageSize;
    }

}
