package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationsPageListDto {

    private List<NotificationDto> notificationsList;

    private PageInfoDto pageInfo;

}
