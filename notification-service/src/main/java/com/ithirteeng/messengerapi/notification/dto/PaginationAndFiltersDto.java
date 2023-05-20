package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginationAndFiltersDto {

    private PageInfoDto pageInfo;

    private NotificationsFiltersDto filters;

}
