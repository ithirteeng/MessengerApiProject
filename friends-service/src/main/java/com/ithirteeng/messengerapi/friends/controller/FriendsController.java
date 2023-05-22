package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.AddDeleteFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.OutputFriendsPageDto;
import com.ithirteeng.messengerapi.friends.service.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Контроллер для запросов сервиса друзей
 */
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    /**
     * Метод для добавления пользователя в друзья
     *
     * @param addDeleteFriendDto ДТО ({@link AddDeleteFriendDto}) для добавления в друзья
     * @param authentication {@link Authentication}
     */
    @PostMapping("/add")
    public void addFriend(@Validated @RequestBody AddDeleteFriendDto addDeleteFriendDto, Authentication authentication) {
        var targetUserData = (JwtUserDetails) authentication.getPrincipal();
        friendsService.addFriend(addDeleteFriendDto.getExternalUserId(), targetUserData.getId());
    }

    /**
     * Метод для получения данных друга
     *
     * @param id идентификатор пользователя
     * @param authentication {@link Authentication}
     * @return {@link FullFriendDto}
     */
    @GetMapping("/{id}")
    public FullFriendDto getFriendData(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return friendsService.getFriendData(userData.getId(), id);
    }

    /**
     * Метод для удаления пользователя из друзей
     *
     * @param addDeleteFriendDto {@link AddDeleteFriendDto}
     * @param authentication {@link FullFriendDto}
     */
    @DeleteMapping("/delete")
    public void deleteFriend(@Validated @RequestBody AddDeleteFriendDto addDeleteFriendDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        friendsService.deleteFriend(addDeleteFriendDto.getExternalUserId(), userData.getId());
    }

    /**
     * Метод для получения списка друзей, отсортированного по параматетрам и пагинацией
     *
     * @param sortingDto ДТО ({@link SortingDto}) со списком фильтров сортировки и информацией по пагинации
     * @param authentication {@link FullFriendDto}
     * @return {@link OutputFriendsPageDto}
     */
    @PostMapping("/list")
    public OutputFriendsPageDto getList(@Valid @RequestBody SortingDto sortingDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return friendsService.getFriendsPage(sortingDto, userData.getId());
    }

    /**
     * Интод для поиска по списку друзей с пагинацией
     *
     * @param searchDto ДТО ({@link SearchDto}) с информацией о пагинации и полем для поиска
     * @param authentication {@link Authentication}
     * @return {@link OutputFriendsPageDto}
     */
    @PostMapping("/search")
    public OutputFriendsPageDto searchFriends(@Valid @RequestBody SearchDto searchDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return friendsService.searchFriends(searchDto, userData.getId());
    }

    /**
     * Метод для обновления фИО пользователя-друга
     *
     * @param id идентификатор пользователя
     * @param authentication {@link Authentication}
     */
    @PatchMapping(value = "/{id}/update")
    public void updateFriendsFullName(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        friendsService.updateFullNameFields(id);
    }

}
