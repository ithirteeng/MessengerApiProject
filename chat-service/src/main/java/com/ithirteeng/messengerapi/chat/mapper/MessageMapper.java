package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.message.SendChatMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendDialogueMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;

import java.util.Date;
import java.util.UUID;

public class MessageMapper {
    public static MessageEntity sendDialogueMessageDtoToEntity(SendDialogueMessageDto sendDialogueMessageDto, ChatEntity chatEntity, UUID targetUserId) {
        return MessageEntity.builder()
                .chatEntity(chatEntity)
                .authorId(targetUserId)
                .messageText(sendDialogueMessageDto.getMessage())
                .creationDate(new Date())
                .build();
    }

    public static MessageEntity sendChatMessageDtoToEntity(SendChatMessageDto sendChatMessageDto, ChatEntity chatEntity, UUID targetUserId) {
        return MessageEntity.builder()
                .chatEntity(chatEntity)
                .authorId(targetUserId)
                .messageText(sendChatMessageDto.getMessage())
                .creationDate(new Date())
                .build();
    }
}
