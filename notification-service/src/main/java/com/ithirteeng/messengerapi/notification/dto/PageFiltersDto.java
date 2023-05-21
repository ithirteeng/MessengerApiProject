package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageFiltersDto {

    private PageInfoDto pageInfo;

    private NotificationsFiltersDto filters;

}
