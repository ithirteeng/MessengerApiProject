package com.ithirteeng.messengerapi.notification.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationService notificationService;

    @GetMapping("/count")
    public Integer getCountOfNotReadNotifications(Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return notificationService.countNotReadNotifications(userData.getId());
    }
}
