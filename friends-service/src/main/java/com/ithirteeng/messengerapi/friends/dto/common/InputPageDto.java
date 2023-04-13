package com.ithirteeng.messengerapi.friends.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * DTO для установки данных для пагинации
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InputPageDto {

    @NotNull(message = "Номер страницы обязателен!")
    private int pageNumber;

    @NotNull(message = "Размер странциы обязателен!")
    private int pageSize;
}
