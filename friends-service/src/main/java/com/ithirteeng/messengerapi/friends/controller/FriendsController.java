package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.friends.dto.friendlist.AddFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.DeleteFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @PostMapping("/add")
    public void addFriend(@Validated @RequestBody AddFriendDto addFriendDto) {
        if (addFriendDto == null) {
            throw new BadRequestException("Тело запроса не должно бтыь пустым");
        }
        friendsService.addFriend(addFriendDto);
    }

    @GetMapping("/{id}/data")
    public FullFriendDto getFriendData(@PathVariable("id") UUID id) {
        return friendsService.getFriendData(UUID.fromString("2e74baa2-29e6-4794-8cb8-2789e270bec1"), id);
    }

    @DeleteMapping("/delete")
    public void deleteFriend(@Validated @RequestBody DeleteFriendDto deleteFriendDto) {
        friendsService.deleteFriend(deleteFriendDto);
    }

    @GetMapping("/test")
    public String test() {
        return "HELLO!";
    }

    @GetMapping("/userex")
    public UUID ifUserExists(Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return (userData.getId());
    }
}
