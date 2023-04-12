package com.ithirteeng.messengerapi.common.service;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для подключения сервиса для пагинации
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CheckPaginationDetailsService.class)
public @interface EnablePaginationService {
}
