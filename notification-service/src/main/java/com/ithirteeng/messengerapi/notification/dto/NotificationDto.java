package com.ithirteeng.messengerapi.notification.dto;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.notification.enums.NotificationStatus;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private UUID notificationId;

    private NotificationType notificationType;

    private String text;

    private NotificationStatus status;

    private String receiveTime;

}
