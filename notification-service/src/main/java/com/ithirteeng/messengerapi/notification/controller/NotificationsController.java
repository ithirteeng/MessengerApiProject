package com.ithirteeng.messengerapi.notification.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.notification.dto.NotificationsPageListDto;
import com.ithirteeng.messengerapi.notification.dto.PageFiltersDto;
import com.ithirteeng.messengerapi.notification.dto.UpdateStatusDto;
import com.ithirteeng.messengerapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для запросов для уведомлений
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    /**
     * Метод для получения количества неподсчитанных уведомлений
     *
     * @param authentication {@link Authentication}
     * @return {@link Integer}
     */
    @GetMapping("/count")
    public Integer getCountOfNotReadNotifications(Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return notificationService.countNotReadNotifications(userData.getId());
    }

    /**
     * Методя для обнолвения статуса у списка уведомлений
     *
     * @param updateStatusDto ДТО ({@link UpdateStatusDto}) для обновления статуса
     * @param authentication {@link Authentication}
     * @return {@link Integer}
     */
    @PostMapping("/status")
    public Integer updateNotificationsStatus(
            @Validated @RequestBody UpdateStatusDto updateStatusDto,
            Authentication authentication
    ) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return notificationService.setStatusToNotifications(updateStatusDto, userData.getId());
    }

    /**
     * Метод для вывода списка уведомлений по страниам, опираясь на данные пагинации
     *
     * @param pageFiltersDto ДТО ({@link PageFiltersDto}) ДТО с фильтрами и параметрами пагинации
     * @param authentication {@link Authentication}
     * @return {@link NotificationsPageListDto}
     */
    @PostMapping("/list")
    public NotificationsPageListDto getNotifications(
            @Validated @RequestBody PageFiltersDto pageFiltersDto,
            Authentication authentication
    ) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return notificationService.getNotifications(pageFiltersDto, userData.getId());
    }
}
