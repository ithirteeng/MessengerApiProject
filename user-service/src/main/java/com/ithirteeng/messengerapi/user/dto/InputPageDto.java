package com.ithirteeng.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO для установки данных для пагинации
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InputPageDto {

    private int pageNumber;

    private int pageSize;
}
