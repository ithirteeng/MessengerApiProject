package com.ithirteeng.messengerapi.common.model;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationDto {

    private UUID userId;

    private NotificationType type;

    private String text;
}
