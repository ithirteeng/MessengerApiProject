package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.friends.service.BlackListService;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Контроллер для интеграционных запросов
 */
@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {
    private final BlackListService blackListService;

    private final FriendsService friendsService;

    /**
     * Методя для проверки, находится ли юзер в ЧС
     *
     * @param externalUserId идентификатор внешнего польщователя
     * @param targetUserId идентификатор целевого пользователя
     * @return {@link Boolean}
     */
    @GetMapping("/blacklist/{externalUserId}/{targetUserId}")
    public Boolean checkIfUserInBlackList(@PathVariable("externalUserId") UUID externalUserId, @PathVariable("targetUserId") UUID targetUserId) {
        return blackListService.checkIfUserInBlackList(targetUserId, externalUserId);
    }

    /**
     * Методя для проверки, находится ли юзер в списки друзей
     *
     * @param externalUserId идентификатор внешнего польщователя
     * @param targetUserId идентификатор целевого пользователя
     * @return {@link Boolean}
     */
    @GetMapping("/friends/{externalUserId}/{targetUserId}")
    public Boolean checkIfUsersAreFriends(@PathVariable("externalUserId") UUID externalUserId, @PathVariable("targetUserId") UUID targetUserId) {
        return friendsService.checkIfUsersAreFriends(targetUserId, externalUserId);
    }
}
