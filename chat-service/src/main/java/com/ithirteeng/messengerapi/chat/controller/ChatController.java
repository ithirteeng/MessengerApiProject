package com.ithirteeng.messengerapi.chat.controller;

import com.ithirteeng.messengerapi.chat.dto.chat.*;
import com.ithirteeng.messengerapi.chat.dto.message.FindMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.OutputMessageDto;
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

/**
 * Контроллер для сообщений
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    private final MessageService messageService;

    /**
     * Метод для создания чата
     *
     * @param createChatDto  ДТО ({@link CreateChatDto}) для создания чата
     * @param authentication {@link Authentication}
     * @return {@link ChatDto}
     */
    @PostMapping("/create")
    public ChatDto createChat(@Validated @RequestBody CreateChatDto createChatDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.createChat(createChatDto, userData.getId());
    }

    /**
     * Метод для обновления чата
     *
     * @param updateChatDto  ДТО ({@link UpdateChatDto}) для обновления чата
     * @param authentication {@link Authentication}
     * @return {@link ChatDto}
     */
    @PostMapping("/update")
    public ChatDto updateChat(@Validated @RequestBody UpdateChatDto updateChatDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.updateChat(updateChatDto, userData.getId());
    }

    /**
     * Метод для получения информации по чату
     *
     * @param chatId         идентификатор чата
     * @param authentication {@link Authentication}
     * @return {@link Object} То есть возвращется либо {@link ShowDialogueDto} либо {@link ShowChatDto}
     */
    @GetMapping("/info/{chatId}")
    public Object getChatInfo(@PathVariable("chatId") UUID chatId, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.getChatInfoById(chatId, userData.getId());
    }

    /**
     * Метод получения сообщений чата
     *
     * @param chatId         идентификатор чата
     * @param authentication {@link Authentication}
     * @return список {@link ShowMessageDto}
     */
    @GetMapping("/messages/{chatId}")
    public List<ShowMessageDto> getChatMessages(@PathVariable("chatId") UUID chatId, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return messageService.getMessagesList(chatId, userData.getId());
    }

    /**
     * Метод для получения списка чатов с пагинацией, поиску и сортировкой по последнему сообщению
     *
     * @param inputChatPageDto ДТО ({@link InputChatPageDto}) с данными о пагинации и поиску
     * @param authentication   {@link Authentication}
     * @return {@link OutputPageChatDto}
     */
    @PostMapping("/list")
    public OutputPageChatDto getChats(@Validated @RequestBody InputChatPageDto inputChatPageDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return chatService.getPage(inputChatPageDto, userData.getId());
    }

    /**
     * Метод для получения сообщений по тексту
     *
     * @param findMessageDto ДТО ({@link FindMessageDto}) для поиска сообщений
     * @param authentication {@link Authentication}
     * @return список {@link OutputMessageDto}
     */
    @PostMapping("/message/find")
    public List<OutputMessageDto> getMessagesByText(@RequestBody FindMessageDto findMessageDto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return messageService.getMessagesByText(findMessageDto, userData.getId());
    }
}
