package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.chat.*;
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
                .creationDate(new Date())
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

    public static ShowChatDto chatEntityToShowChatDto(ChatEntity entity) {
        return ShowChatDto.builder()
                .chatName(entity.getChatName())
                .creationDate(entity.getCreationDate())
                .chatAdmin(entity.getChatAdmin())
                .avatarId(entity.getAvatarId())
                .build();
    }

    public static ShowDialogueDto chatEntityToShowDialogueDto(ChatEntity entity, String username) {
        return ShowDialogueDto.builder()
                .chatName(username)
                .creationDate(entity.getCreationDate())
                .build();
    }


}
