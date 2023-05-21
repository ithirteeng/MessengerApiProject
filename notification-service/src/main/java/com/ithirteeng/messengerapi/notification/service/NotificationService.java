package com.ithirteeng.messengerapi.notification.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.dto.UpdateStatusDto;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import com.ithirteeng.messengerapi.notification.mapper.NotificationMapper;
import com.ithirteeng.messengerapi.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
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
            throw new BadRequestException("Такие уведомления у пользователя отсутсвтуют: " + invalidIds);
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
}
