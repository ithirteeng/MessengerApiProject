package com.ithirteeng.messengerapi.friends.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * ДТО для поиска по фильтрам с пагинацией
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SortingDto {

    @NotNull(message = "Информация о пагинации обязательна!")
    @Valid
    private InputPageDto pageInfo;

    @NotNull(message = "Фильтры обязательны!")
    @Valid
    private PageFiltersDto filters;
}
