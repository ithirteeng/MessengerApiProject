package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.message.OutputMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendChatMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendDialogueMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.ShowMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;

import java.util.Date;
import java.util.UUID;

/**
 * Маппер для сообщений
 */
public class MessageMapper {
    /**
     * Метод для преобразвания объекта типа {@link SendDialogueMessageDto} в объект типа {@link MessageEntity}
     *
     * @param sendDialogueMessageDto объект типа {@link SendDialogueMessageDto}
     * @param chatEntity объект типа {@link ChatEntity}
     * @param targetUserId идентификатор пользователя
     * @return {@link MessageEntity}
     */
    public static MessageEntity sendDialogueMessageDtoToEntity(SendDialogueMessageDto sendDialogueMessageDto, ChatEntity chatEntity, UUID targetUserId) {
        return MessageEntity.builder()
                .chatEntity(chatEntity)
                .authorId(targetUserId)
                .messageText(sendDialogueMessageDto.getMessage())
                .creationDate(new Date())
                .build();
    }

    /**
     * Метод для преобразвания объекта типа {@link SendChatMessageDto} в объект типа {@link MessageEntity}
     *
     * @param sendChatMessageDto объект типа {@link SendChatMessageDto}
     * @param chatEntity объект типа {@link ChatEntity}
     * @param targetUserId идентификатор пользователя
     * @return {@link MessageEntity}
     */
    public static MessageEntity sendChatMessageDtoToEntity(SendChatMessageDto sendChatMessageDto, ChatEntity chatEntity, UUID targetUserId) {
        return MessageEntity.builder()
                .chatEntity(chatEntity)
                .authorId(targetUserId)
                .messageText(sendChatMessageDto.getMessage())
                .creationDate(new Date())
                .build();
    }

    /**
     * Метод для преобразвания объекта типа {@link MessageEntity} в объект типа {@link ShowMessageDto}
     *
     * @param messageEntity объект типа {@link MessageEntity}
     * @param userName имя пользователя
     * @param userAvatar идентификатор аватарки пользователя
     * @return {@link ShowMessageDto}
     */
    public static ShowMessageDto entityToshowMessageDto(MessageEntity messageEntity, String userName, UUID userAvatar) {
        return ShowMessageDto.builder()
                .messageId(messageEntity.getId())
                .avatarId(userAvatar)
                .senderName(userName)
                .sendingTime(messageEntity.getCreationDate())
                .message(messageEntity.getMessageText())
                .build();
    }

    /**
     * Метод для преобразвания объекта типа {@link MessageEntity} в объект типа {@link OutputMessageDto}
     *
     * @param messageEntity объект типа {@link MessageEntity}
     * @param chatName имя чата
     * @param fileName имя файла
     * @return {@link OutputMessageDto}
     */
    public static OutputMessageDto entityToOutputMessageDto(MessageEntity messageEntity, String chatName, String fileName) {
        return OutputMessageDto.builder()
                .chatId(messageEntity.getChatEntity().getId())
                .sendingDate(messageEntity.getCreationDate())
                .fileName(fileName)
                .chatName(chatName)
                .message(messageEntity.getMessageText())
                .build();
    }
}
