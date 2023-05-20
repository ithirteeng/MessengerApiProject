package com.ithirteeng.messengerapi.notification.dto;

import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateNotificationStatusDto {

    @NotEmpty(message = "Список id уведомлений не может быть пустым!")
    private List<UUID> notificationsIDs;

    @NotNull(message = "Статус сообщений обязателен!")
    private NotificationStatus status;
}
