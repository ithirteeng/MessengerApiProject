package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.friends.dto.friendlist.AddFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/friends")
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
}
