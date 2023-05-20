package com.ithirteeng.messengerapi.friends.stream;

import com.ithirteeng.messengerapi.friends.service.BlackListService;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitUserEventListener {

    private final FriendsService friendsService;
    private final BlackListService blackListService;

    @Bean
    public Consumer<UUID> userDataSyncEvent() {
        return userId -> {
            log.info("username update");
            friendsService.updateFullNameFields(userId);
            blackListService.updateFullNameFields(userId);
        };
    }
}
