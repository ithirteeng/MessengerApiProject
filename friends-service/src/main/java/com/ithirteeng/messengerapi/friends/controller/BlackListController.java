package com.ithirteeng.messengerapi.friends.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.friends.dto.blacklist.AddDeleteNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.FullNoteDto;
import com.ithirteeng.messengerapi.friends.service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

}
