package com.ithirteeng.messengerapi.notification.dto;

import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * ДТО для обновления статусов уведомлений
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateStatusDto {

    @NotEmpty(message = "Список id уведомлений не может быть пустым!")
    private List<UUID> notificationsList;

    @NotNull(message = "Статус сообщений обязателен!")
    private NotificationStatus status;
}
