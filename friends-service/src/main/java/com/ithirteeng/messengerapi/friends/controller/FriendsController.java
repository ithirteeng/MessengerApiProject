package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.AddDeleteFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.OutputFriendsPageDto;
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
    public void addFriend(@Validated @RequestBody AddDeleteFriendDto addDeleteFriendDto, Authentication authentication) {
        var targetUserData = (JwtUserDetails) authentication.getPrincipal();
        friendsService.addFriend(addDeleteFriendDto.getExternalUserId(), targetUserData.getId());
    }

    @GetMapping("/{id}")
    public FullFriendDto getFriendData(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return friendsService.getFriendData(userData.getId(), id);
    }

    @DeleteMapping("/delete")
    public void deleteFriend(@Validated @RequestBody AddDeleteFriendDto addDeleteFriendDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        friendsService.deleteFriend(addDeleteFriendDto.getExternalUserId(), userData.getId());
    }

    @PostMapping("/list")
    public OutputFriendsPageDto test(@Validated @RequestBody SortingDto sortingDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return friendsService.getFriendsList(sortingDto, userData.getId());
    }

}
