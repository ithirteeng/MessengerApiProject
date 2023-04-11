package com.ithirteeng.messengerapi.common.exception;

/**
 * Класс ошибки.
 * UnauthorizedException используется тогда, когда пользователь не авторизован, а запрос требует авторизации
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}