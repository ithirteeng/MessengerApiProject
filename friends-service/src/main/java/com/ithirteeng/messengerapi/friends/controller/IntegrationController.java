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

    @GetMapping("/blacklist/{externalUserId}/{targetUserId}")
    public Boolean checkIfUserInBlackList(@PathVariable("externalUserId") UUID externalUserId, @PathVariable("targetUserId") UUID targetUserId) {
        return blackListService.checkIfUserInBlackList(targetUserId, externalUserId);
    }

    @GetMapping("/friends/{externalUserId}/{targetUserId}")
    public Boolean checkIfUsersAreFriends(@PathVariable("externalUserId") UUID externalUserId, @PathVariable("targetUserId") UUID targetUserId) {
        return friendsService.checkIfUsersAreFriends(targetUserId, externalUserId);
    }
}
