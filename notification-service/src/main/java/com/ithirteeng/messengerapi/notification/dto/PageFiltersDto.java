package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

/**
 * ДТО для информации о пагинации и фильтрах выборки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageFiltersDto {

    private PageInfoDto pageInfo;

    private NotificationsFiltersDto filters;

}
