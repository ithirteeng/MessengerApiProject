package com.ithirteeng.messengerapi.common.exception;

/**
 * Класс ошибки.
 * FileException используется тогда, когда возникает проблема с загрузкой/скачиванием файлов
 */
public class FileException extends RuntimeException {
    public FileException(String message, Exception e) {
        super(message, e);
    }
}
