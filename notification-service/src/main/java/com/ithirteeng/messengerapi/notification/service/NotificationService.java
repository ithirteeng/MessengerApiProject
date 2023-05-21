package com.ithirteeng.messengerapi.notification.service;

import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import com.ithirteeng.messengerapi.notification.mapper.NotificationMapper;
import com.ithirteeng.messengerapi.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
