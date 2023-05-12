package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.chat.ChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.CreateChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.UpdateChatDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;

import java.util.Date;
import java.util.UUID;

public class ChatMapper {
    public static ChatEntity createChatDtoToChatEntity(CreateChatDto dto, UUID adminId) {
        return ChatEntity.builder()
                .chatName(dto.getChatName())
                .chatAdmin(adminId)
                .creationDate(new Date())
                .isDialog(false)
                .avatarId(dto.getAvatarId())
                .build();
    }

    public static ChatEntity dialogueToChatEntity() {
        return ChatEntity.builder()
                .isDialog(true)
                .build();
    }

    public static ChatEntity updateChatDtoToChatEntity(UpdateChatDto dto, ChatEntity entity) {
        return ChatEntity.builder()
                .chatName(dto.getChatName())
                .chatAdmin(entity.getChatAdmin())
                .creationDate(entity.getCreationDate())
                .id(entity.getId())
                .isDialog(entity.getIsDialog())
                .avatarId(dto.getAvatarId())
                .build();
    }

    public static ChatDto chatEntityToChatDto(ChatEntity entity) {
        return ChatDto.builder()
                .chatName(entity.getChatName())
                .chatAdmin(entity.getChatAdmin())
                .creationDate(entity.getCreationDate())
                .id(entity.getId())
                .isDialog(entity.getIsDialog())
                .avatarId(entity.getAvatarId())
                .build();
    }


}
