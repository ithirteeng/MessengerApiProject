package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.friends.dto.blacklist.AddDeleteNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.FullNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.OutputNotesPageDto;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * Контроллер для запросов сервиса черного списка
 */
@RestController
@RequestMapping("/api/blacklist")
@RequiredArgsConstructor
public class BlackListController {

    private final BlackListService blackListService;

    @PostMapping("/add")
    public void addNote(@Validated @RequestBody AddDeleteNoteDto addDeleteNoteDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        blackListService.addNote(addDeleteNoteDto.getExternalUserId(), userData.getId());
    }

    @GetMapping("{id}")
    public FullNoteDto getBlockedUserData(@PathVariable("id") UUID externalUserId, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.getNoteData(userData.getId(), externalUserId);
    }

    @DeleteMapping("/delete")
    public void deleteNote(@Validated @RequestBody AddDeleteNoteDto addDeleteNoteDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        blackListService.deleteNote(addDeleteNoteDto.getExternalUserId(), userData.getId());
    }

    @PostMapping("/list")
    public OutputNotesPageDto getList(@Valid @RequestBody SortingDto sortingDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.getNotes(sortingDto, userData.getId());
    }

    @PostMapping("/search")
    public OutputNotesPageDto searchFriends(@Valid @RequestBody SearchDto searchDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.searchNotes(searchDto, userData.getId());
    }

    @PatchMapping(value = "/{id}/update")
    public void updateFriendsFullName(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        blackListService.updateFullNameFields(id);
    }

    @GetMapping(value = "/check/{id}")
    public Boolean checkUserExisting(@PathVariable("id") UUID id, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return blackListService.checkIfUserInBlackList(userData.getId(), id);
    }

}
