package com.ithirteeng.messengerapi.common.controller;

import com.ithirteeng.messengerapi.common.exception.UnauthorizedException;
import com.ithirteeng.messengerapi.common.model.ApiErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {

    /**
     * Метод для отлавливания всех {@link UnauthorizedException}.
     *
     * @param exception исключение.
     * @param request   запрос, в ходе выполнения которого возникло исключение.
     * @return объект класса {@link ApiErrorModel} со статус кодом 401.
     */
    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(
            UnauthorizedException exception,
            WebRequest request
    ) {
        logError(request, exception);
        return new ResponseEntity<>(
                new ApiErrorModel(exception.getMessage(), HttpStatus.UNAUTHORIZED.value()),
                HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Метод для логирования ошибок, доходящих до контроллера
     *
     * @param request   запрос, в ходе выполнения которого возникло исключение
     * @param exception исключение
     */
    private void logError(WebRequest request, Exception exception) {
        log.error("Произошла ошибка на запросе {}", request.getDescription(true));
        log.error(exception.getMessage(), exception);
    }
}
