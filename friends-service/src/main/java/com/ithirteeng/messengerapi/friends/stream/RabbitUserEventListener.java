package com.ithirteeng.messengerapi.friends.stream;

import com.ithirteeng.messengerapi.friends.service.BlackListService;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Класс-обработчик событий, связанных с новыми уведомлениями из RabbitMQ
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitUserEventListener {

    private final FriendsService friendsService;
    private final BlackListService blackListService;

    /**
     * Метод для обрабатки событий синхронизации данных пользователя. Обновляет имя отправителя у сообщений,
     * которые были отправлены пользователем с указанным идентификатором
     *
     * @return {@link Consumer}<{@link UUID}> - consumer, обрабатывающий событие синхронизации данных пользователя
     */
    @Bean
    public Consumer<UUID> userDataSyncEvent() {
        return userId -> {
            log.info("username update");
            friendsService.updateFullNameFields(userId);
            blackListService.updateFullNameFields(userId);
        };
    }
}
