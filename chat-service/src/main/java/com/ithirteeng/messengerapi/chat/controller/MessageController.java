package com.ithirteeng.messengerapi.chat.controller;

import com.ithirteeng.messengerapi.chat.dto.message.SendChatMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendDialogueMessageDto;
import com.ithirteeng.messengerapi.chat.service.MessageService;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/send/{userId}")
    public void sendMessage(@PathVariable("userId") UUID id, @Validated @RequestBody SendDialogueMessageDto sendDialogueMessageDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        sendDialogueMessageDto.setUserId(id);
        messageService.sendMessageInDialogue(sendDialogueMessageDto, userData.getId());
    }

    @PostMapping("/send")
    public void sendMessage(@Validated @RequestBody SendChatMessageDto sendChatMessageDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        messageService.sendMessageInChat(sendChatMessageDto, userData.getId());
    }
}
