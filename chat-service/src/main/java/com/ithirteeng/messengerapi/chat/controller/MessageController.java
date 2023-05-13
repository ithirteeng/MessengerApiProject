package com.ithirteeng.messengerapi.chat.controller;

import com.ithirteeng.messengerapi.chat.dto.message.SendMessageDto;
import com.ithirteeng.messengerapi.chat.service.MessageService;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/send")
    public void sendMessage(@Validated @RequestBody SendMessageDto sendMessageDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        messageService.sendMessage(sendMessageDto, userData.getId());
    }
}
