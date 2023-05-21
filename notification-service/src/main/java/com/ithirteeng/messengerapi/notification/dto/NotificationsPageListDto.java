package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

import java.util.List;

/**
 * ДТО для показа списка уведомлений с данными о пагинации
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationsPageListDto {

    private List<NotificationDto> notificationsList;

    private PageInfoDto pageInfo;

}
