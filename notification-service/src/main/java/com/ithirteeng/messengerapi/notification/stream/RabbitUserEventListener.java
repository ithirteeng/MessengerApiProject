package com.ithirteeng.messengerapi.notification.stream;

import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitUserEventListener {

    private final NotificationService notificationService;

    @Bean
    public Consumer<CreateNotificationDto> notificationEvent() {
        return notificationService::createNotification;
    }
}
