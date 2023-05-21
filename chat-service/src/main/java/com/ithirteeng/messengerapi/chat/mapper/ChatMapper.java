package com.ithirteeng.messengerapi.chat.mapper;

import com.ithirteeng.messengerapi.chat.dto.chat.*;
import com.ithirteeng.messengerapi.chat.dto.common.PageInfoDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Маппер для чатов
 */
public class ChatMapper {
    /**
     * Метод для преобразования объекта типа {@link CreateChatDto} в объект типа {@link ChatEntity}
     *
     * @param dto в объект типа {@link CreateChatDto}
     * @param adminId идентификатор админа чата
     * @return объект типа {@link ChatEntity}
     */
    public static ChatEntity createChatDtoToChatEntity(CreateChatDto dto, UUID adminId) {
        return ChatEntity.builder()
                .chatName(dto.getChatName())
                .chatAdmin(adminId)
                .creationDate(new Date())
                .isDialog(false)
                .avatarId(dto.getAvatarId())
                .build();
    }

    /**
     * Метод для преобразования диалога в объект типа {@link ChatEntity}
     *
     * @return {@link ChatEntity}
     */
    public static ChatEntity dialogueToChatEntity() {
        return ChatEntity.builder()
                .isDialog(true)
                .creationDate(new Date())
                .build();
    }

    /**
     * Метод для преобразования объекта типа {@link UpdateChatDto} в объект типа {@link ChatEntity}
     *
     * @param dto объект типа {@link UpdateChatDto}
     * @param entity объект типа {@link ChatEntity}
     * @return {@link ChatEntity}
     */
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

    /**
     * Метод для преобразования объекта типа {@link ChatEntity} в объект типа {@link ChatDto}
     *
     * @param entity объект типа {@link ChatEntity}
     * @return {@link ChatDto}
     */
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

    /**
     * Метод для преобразования объекта типа {@link ChatEntity} в объект типа {@link ShowChatDto}
     *
     * @param entity объект типа {@link ChatEntity}
     * @return {@link ShowChatDto}
     */
    public static ShowChatDto chatEntityToShowChatDto(ChatEntity entity) {
        return ShowChatDto.builder()
                .chatName(entity.getChatName())
                .creationDate(entity.getCreationDate())
                .chatAdmin(entity.getChatAdmin())
                .avatarId(entity.getAvatarId())
                .build();
    }

    /**
     * Метод для преобразования объекта типа {@link ChatEntity} в объект типа {@link ShowDialogueDto}
     *
     * @param entity объект типа {@link ChatEntity}
     * @param username имя пользователя
     * @return {@link ShowDialogueDto}
     */
    public static ShowDialogueDto chatEntityToShowDialogueDto(ChatEntity entity, String username) {
        return ShowDialogueDto.builder()
                .chatName(username)
                .creationDate(entity.getCreationDate())
                .build();
    }

    /**
     * Метод для создания объекта типа {@link OutputPageChatDto}
     *
     * @param list список {@link PageChatDto}
     * @param pageInfoDto ДТО с информацией о пагинации типа {@link PageInfoDto}
     * @param totalPages количество страниц
     * @return {@link OutputPageChatDto}
     */
    public static OutputPageChatDto getPageFromData(List<PageChatDto> list, PageInfoDto pageInfoDto, Integer totalPages) {
        return OutputPageChatDto.builder()
                .chats(list)
                .totalPages(totalPages)
                .pageNumber(pageInfoDto.getPageNumber())
                .pageSize(pageInfoDto.getPageSize())
                .build();
    }



}
