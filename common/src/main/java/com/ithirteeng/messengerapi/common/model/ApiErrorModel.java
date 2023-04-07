package com.ithirteeng.messengerapi.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для вывода ошибки
 * ApiErrorModel стоит использовать тогда, когда вам нужно вывести ошибку
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorModel {

    private String errorMessage;

    private int statusCode;
}
