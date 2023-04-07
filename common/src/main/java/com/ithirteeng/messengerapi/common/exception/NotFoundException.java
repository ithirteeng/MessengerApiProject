package com.ithirteeng.messengerapi.common.exception;

/**
 * Класс ошибки.
 * NotFoundException используется тогда, когда по запросу пользователя ничего не найдено
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
