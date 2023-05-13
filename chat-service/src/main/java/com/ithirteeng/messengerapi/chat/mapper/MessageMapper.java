package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.message.SendMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;

import java.util.Date;
import java.util.UUID;

public class MessageMapper {
    public static MessageEntity sendMessageDtoToEntity(SendMessageDto sendMessageDto, ChatEntity chatEntity, UUID targetUserId) {
        return MessageEntity.builder()
                .chatEntity(chatEntity)
                .authorId(targetUserId)
                .messageText(sendMessageDto.getMessage())
                .creationDate(new Date())
                .build();
    }
}
