package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.friends.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/integration/blacklist")
@RequiredArgsConstructor
public class IntegrationController {
    private final BlackListService blackListService;

    @GetMapping("/{externalUserId}/{targetUserId}")
    public Boolean checkIfUserInBlackList(@PathVariable("externalUserId") UUID externalUserId, @PathVariable("targetUserId") UUID targetUserId) {
        return blackListService.checkIfUserInBlackList(targetUserId, externalUserId);
    }
}
