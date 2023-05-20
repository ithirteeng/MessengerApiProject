package com.ithirteeng.messengerapi.notification.dto;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import lombok.*;

import java.util.List;


/**
 * DTO для фильтрации уведомлений. Объект содержит фильтры по периоду, тексту и типам уведомлений.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationsFiltersDto {

    /**
     * Фильтр по периоду, содержащий начальную и конечную даты.
     */
    private PeriodDto periodFilter;

    /**
     * Фильтр по тексту уведомления.
     */
    private String textFilter;

    /**
     * Список типов уведомлений для фильтрации.
     */
    private List<NotificationType> types;

}
