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
    @NotNull
    private InputPageDto pageInfo;

    @NotNull
    private SortingFiltersDto filters;

    private SortingFieldsDto fields;
}
