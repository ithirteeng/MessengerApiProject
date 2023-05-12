package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.dto.chat.ChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.CreateChatDto;
import com.ithirteeng.messengerapi.chat.dto.chat.UpdateChatDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.mapper.ChatMapper;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final CommonService commonService;

    @Transactional
    public void sendMessage(SendMessageDto sendMessageDto, UUID targetUserId) {
        commonService.checkUserExisting(sendMessageDto.getUserId());
        commonService.checkIfUserInBlackList(sendMessageDto.getUserId(), targetUserId);
        commonService.checkIfUserInBlackList(targetUserId, sendMessageDto.getUserId());

        var entity = ChatMapper.dialogueToChatEntity();
        addChatToUser(entity, sendMessageDto.getUserId());
        addChatToUser(entity, targetUserId);

        // todo: add ability of sending message
    }

    @Transactional
    public void addChatToUser(ChatEntity chatEntity, UUID userId) {
        if (!chatUserRepository.existsChatUserByUserIdAndChatEntity(userId, chatEntity)) {
            chatUserRepository.save(
                    ChatUserEntity.builder()
                            .id(UUID.randomUUID())
                            .chatEntity(chatEntity)
                            .userId(userId)
                            .build()
            );
        }

    }

    @Transactional
    public ChatDto createChat(CreateChatDto createChatDto, UUID targetUserId) {
        var entity = ChatMapper.createChatDtoToChatEntity(createChatDto, targetUserId);

        chatRepository.save(entity);

        addUsersToChat(createChatDto.getUsersIdsList(), targetUserId, entity);

        return ChatMapper.chatEntityToChatDto(entity);
    }

    @Transactional
    public ChatDto updateChat(UpdateChatDto updateChatDto, UUID targetUserId) {
        var entity = chatRepository.findById(updateChatDto.getChatId())
                .orElseThrow(() -> new NotFoundException("Такого чата не существует"));
        entity.getChatUserEntitiesList().clear();
        chatUserRepository.deleteAllByChatEntity(entity);

        var updatedEntity = ChatMapper.updateChatDtoToChatEntity(updateChatDto, entity);
        chatRepository.save(updatedEntity);
        addUsersToChat(updateChatDto.getUsersIdsList(), targetUserId, updatedEntity);

        return ChatMapper.chatEntityToChatDto(updatedEntity);
    }

    private void addUsersToChat(List<UUID> usersIdsList, UUID targetUserId, ChatEntity chatEntity) {
        for (UUID userId : usersIdsList) {
            commonService.checkUserExisting(userId);
            commonService.checkIfUserInBlackList(userId, targetUserId);
            commonService.checkIfUserInBlackList(targetUserId, userId);
            if (userId.equals(targetUserId)) {
                throw new BadRequestException("Пользователь автоматически добавляется в список участников");
            }

            addChatToUser(chatEntity, userId);
        }
        addChatToUser(chatEntity, targetUserId);
    }
}
