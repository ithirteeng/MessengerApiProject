package com.ithirteeng.messengerapi.storage.controller;

import com.ithirteeng.messengerapi.common.exception.*;
import com.ithirteeng.messengerapi.common.model.ApiErrorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для отлавливания ошибок
 */
@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {


    /**
     * Метод для отлавливания всех {@link NotFoundException}.
     *
     * @param exception исключение.
     * @param request   запрос, в ходе выполнения которого возникло исключение.
     * @return объект класса {@link ApiErrorModel} со статус кодом 404.
     */
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(
            NotFoundException exception,
            WebRequest request
    ) {
        logError(request, exception);
        return new ResponseEntity<>(
                new ApiErrorModel(exception.getMessage(), HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND
        );
    }

    /**
     * Метод для отлавливания всех {@link ConflictException}.
     *
     * @param exception исключение.
     * @param request   запрос, в ходе выполнения которого возникло исключение.
     * @return объект класса {@link ApiErrorModel} со статус кодом 409.
     */
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(
            ConflictException exception,
            WebRequest request
    ) {
        logError(request, exception);
        return new ResponseEntity<>(
                new ApiErrorModel(exception.getMessage(), HttpStatus.CONFLICT.value()),
                HttpStatus.CONFLICT
        );
    }

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
     * Метод для обработки исключений для невалидных тел запросов.
     *
     * @param exception исключение.
     * @param headers   заголовки, которые будут записаны в ответ.
     * @param status    выбранный статус ответа.
     * @param request   текущий запрос.
     * @return {@link ResponseEntity}, где ключ - название поля невалидного тела запроса,
     * а значение - список {@code user-friendly} сообщений об ошибке.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        logError(request, exception);
        List<String> details = new ArrayList<>();
        for(ObjectError error : exception.getBindingResult().getAllErrors()) {
            details.add(((FieldError) error).getField() +": " + error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Метод для отлавливания всех {@link BadRequestException}.
     *
     * @param exception исключение.
     * @param request   запрос, в ходе выполнения которого возникло исключение.
     * @return объект класса {@link ApiErrorModel} со статус кодом 400.
     */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(
            BadRequestException exception,
            WebRequest request
    ) {
        logError(request, exception);
        return new ResponseEntity<>(
                new ApiErrorModel(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Метод для отлавливания всех {@link FileException}.
     *
     * @param exception исключение.
     * @param request   запрос, в ходе выполнения которого возникло исключение.
     * @return объект класса {@link ApiErrorModel} со статус кодом 400.
     */
    @ExceptionHandler(FileException.class)
    protected ResponseEntity<Object> handleFileException(
            FileException exception,
            WebRequest request
    ) {
        logError(request, exception);
        return new ResponseEntity<>(
                new ApiErrorModel(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Метод для отлавливания всех {@link MultipartException}.
     *
     * @param exception исключение.
     * @param request   запрос, в ходе выполнения которого возникло исключение.
     * @return объект класса {@link ApiErrorModel} со статус кодом 400.
     */
    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartException(
            MultipartException exception,
            WebRequest request
    ) {
        logError(request, exception);
        return new ResponseEntity<>(
                new ApiErrorModel(exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Метод для обработки исключений для невалидных тел запросов.
     *
     * @param ex исключение.
     * @param headers   заголовки, которые будут записаны в ответ.
     * @param status    выбранный статус ответа.
     * @param request   текущий запрос.
     * @return {@link ResponseEntity}, где ключ - название поля невалидного тела запроса,
     * а значение - список {@code user-friendly} сообщений об ошибке.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        logError(request, ex);
        List<String> details = new ArrayList<>();
        details.add("JSON fields are invalid!");
        ErrorResponse error = new ErrorResponse("JSON parse error", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Метод для логирования ошибок, доходящих до контроллера
     *
     * @param request   запрос, в ходе выполнения которого возникло исключение
     * @param exception исключение
     */
    private void logError(WebRequest request, Exception exception) {
        log.error("Произошла ошибка на запросе {}", request.getDescription(false));
        log.error(exception.getMessage());
    }
}
