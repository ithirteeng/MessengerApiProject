package com.ithirteeng.messengerapi.chat.controller;

import com.ithirteeng.messengerapi.chat.dto.chat.ChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.CreateChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.UpdateChatDto;
import com.ithirteeng.messengerapi.chat.service.ChatService;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ChatDto createChat(@Validated @RequestBody CreateChatDto createChatDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.createChat(createChatDto, userData.getId());
    }

    @PostMapping("/update")
    public ChatDto updateChat(@Validated @RequestBody UpdateChatDto updateChatDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.updateChat(updateChatDto, userData.getId());
    }
}
