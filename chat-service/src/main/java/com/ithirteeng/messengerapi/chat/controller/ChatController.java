package com.ithirteeng.messengerapi.chat.controller;

import com.ithirteeng.messengerapi.chat.dto.chat.ChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.CreateChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.UpdateChatDto;
import com.ithirteeng.messengerapi.chat.dto.message.ShowMessageDto;
import com.ithirteeng.messengerapi.chat.service.ChatService;
import com.ithirteeng.messengerapi.chat.service.MessageService;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final MessageService messageService;

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

    @GetMapping("/info/{chatId}")
    public Object getChatInfo(@PathVariable("chatId") UUID chatId, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.getChatInfoById(chatId, userData.getId());
    }

    @GetMapping("/messages/{chatId}")
    public List<ShowMessageDto> getChatMessages(@PathVariable("chatId") UUID chatId) {
        return messageService.getMessagesList(chatId);
    }
}
