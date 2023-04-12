package com.ithirteeng.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * DTO для запроса списка пользователей по параметрам
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SortingDto {
    @NotNull(message = "Информация о пагинации обязательна!")
    private InputPageDto pageInfo;

    @NotNull(message = "Фильтры обязательны!")
    private SortingFiltersDto filters;

    @NotNull(message = "Поля сортировки обязательны!")
    private SortingFieldsDto fields;
}
