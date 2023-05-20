package com.ithirteeng.messengerapi.notification.service;

import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.mapper.NotificationMapper;
import com.ithirteeng.messengerapi.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(CreateNotificationDto dto) {
        var entity = NotificationMapper.entityFromCreateDto(dto);
        notificationRepository.save(entity);
    }
}
