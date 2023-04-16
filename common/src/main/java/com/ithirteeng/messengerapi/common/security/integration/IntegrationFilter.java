package com.ithirteeng.messengerapi.common.security.integration;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Фильтр аутентификации для интеграционных запросов
 */
@RequiredArgsConstructor
@Slf4j
public class IntegrationFilter extends OncePerRequestFilter {

    private final String apiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!Objects.equals(request.getHeader(RequestsConstants.API_KEY_HEADER), apiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            logError(request, new UnauthorizedException("Отсутсвует хэдер API_KEY!"));
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(new IntegrationAuthentication());
        filterChain.doFilter(request, response);

    }

    private void logError(HttpServletRequest request, Exception exception) {
        log.error("Произошла ошибка на запросе {}", request.getRequestURL());
        log.error(exception.getMessage());
    }
}
