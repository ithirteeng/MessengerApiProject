package com.ithirteeng.messengerapi.notification.dto;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import lombok.*;

import java.util.List;

/**
 * ДТО для фильтров выборки уведомлений
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationsFiltersDto {

    private DatePeriodDto periodFilter;

    private String textFilter;

    private List<NotificationType> types;

}
