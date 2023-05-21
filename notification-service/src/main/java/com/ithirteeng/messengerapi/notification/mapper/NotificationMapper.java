package com.ithirteeng.messengerapi.notification.mapper;

import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.dto.NotificationDto;
import com.ithirteeng.messengerapi.notification.entity.NotificationEntity;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Маппер для уведомлений
 */
public class NotificationMapper {
    /**
     * Метод для преобразования объекта типа {@link CreateNotificationDto} в объект типа {@link NotificationEntity}
     *
     * @param dto объект типа {@link CreateNotificationDto}
     * @return {@link NotificationEntity}
     */
    public static NotificationEntity entityFromCreateDto(CreateNotificationDto dto) {
        return NotificationEntity.builder()
                .userId(dto.getUserId())
                .type(dto.getType())
                .status(NotificationStatus.NOT_READ)
                .receiveTime(LocalDateTime.now())
                .text(dto.getText())
                .build();
    }

    /**
     * Метод для преобразования объекта типа {@link NotificationEntity} в объект типа {@link NotificationDto}
     *
     * @param entity объект типа {@link NotificationEntity}
     * @return {@link NotificationDto}
     */
    public static NotificationDto dtoFromEntity(NotificationEntity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = entity.getReceiveTime().format(formatter);
        return NotificationDto.builder()
                .notificationId(entity.getId())
                .status(entity.getStatus())
                .notificationType(entity.getType())
                .receiveTime(formattedDateTime)
                .text(entity.getText())
                .build();
    }
}
