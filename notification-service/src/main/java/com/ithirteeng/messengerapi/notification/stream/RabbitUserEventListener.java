package com.ithirteeng.messengerapi.notification.stream;

import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Класс-обработчик событий, связанных с новыми уведомлениями из RabbitMQ
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitUserEventListener {

    private final NotificationService notificationService;

    /**
     * Метод для обрабатки событий создания уведомлений. Создает новое уведомление по ДТО {@link CreateNotificationDto}
     *
     * @return {@link Consumer}<{@link CreateNotificationDto}> - consumer, обрабатывающий событие создания уведомления
     */
    @Bean
    public Consumer<CreateNotificationDto> notificationEvent() {
        return notificationService::createNotification;
    }
}
