package com.ithirteeng.messengerapi.friends.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SortingDto {
    @NotNull(message = "Информация о пагинации обязательна!")
    private InputPageDto pageInfo;

    @NotNull(message = "Фильтры обязательны!")
    private PageFiltersDto filters;


}
