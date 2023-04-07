package com.ithirteeng.messengerapi.common.exception;

/**
 * Класс ошибки.
 * ConflictException используется, когда запрос пользователя как-то конфликтует с текущим состоянием
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
