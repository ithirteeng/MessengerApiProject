package com.ithirteeng.messengerapi.chat.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * ДТО для установки данных для пагинации
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PageInfoDto {

    @NotNull(message = "Номер страницы обязателен!")
    private Integer pageNumber;

    @NotNull(message = "Размер страницы обязателен!")
    private Integer pageSize;
}
