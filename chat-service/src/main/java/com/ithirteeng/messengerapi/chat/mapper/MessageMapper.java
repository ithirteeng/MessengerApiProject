package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.message.SendChatMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendDialogueMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.ShowMessageDto;
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

    public static ShowMessageDto entityToshowMessageDto(MessageEntity messageEntity, String userName, UUID userAvatar) {
        return ShowMessageDto.builder()
                .messageId(messageEntity.getId())
                .avatarId(userAvatar)
                .senderName(userName)
                .sendingTime(messageEntity.getCreationDate())
                .message(messageEntity.getMessageText())
                .build();
    }
}
