package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.dto.message.SendMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.mapper.ChatMapper;
import com.ithirteeng.messengerapi.chat.mapper.MessageMapper;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final CommonService commonService;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    @Transactional
    public void sendMessage(SendMessageDto sendMessageDto, UUID targetUserId) {
        if (sendMessageDto.getUserId().equals(targetUserId)) {
            throw new BadRequestException("Пользователь не может отослать сам себе сообщение!");
        }
        commonService.checkUserExisting(sendMessageDto.getUserId());
        commonService.checkIfUserInBlackList(sendMessageDto.getUserId(), targetUserId);
        commonService.checkIfUserInBlackList(targetUserId, sendMessageDto.getUserId());
        commonService.checkIfUsersAreFriends(sendMessageDto.getUserId(), targetUserId);

        var entity = createDialogue(sendMessageDto, targetUserId);

        var messageEntity = MessageMapper.sendMessageDtoToEntity(sendMessageDto, entity, targetUserId);
        messageRepository.save(messageEntity);
    }

    @Transactional
    ChatEntity createDialogue(SendMessageDto sendMessageDto, UUID targetUserId) {
        var result = checkIfDialogueCreated(sendMessageDto.getUserId(), targetUserId);
        if (result == null) {
            var entity = ChatMapper.dialogueToChatEntity();

            chatRepository.save(entity);

            addChatToUser(entity, sendMessageDto.getUserId());
            addChatToUser(entity, targetUserId);

            return entity;
        } else {
            return result;
        }
    }

    @Transactional
    ChatEntity checkIfDialogueCreated(UUID externalUserId, UUID targetUserId) {
        var targetList = chatUserRepository.findAllByUserId(targetUserId);
        var externalList = chatUserRepository.findAllByUserId(externalUserId);


        if (targetList.isEmpty() || externalList.isEmpty()) {
            return null;
        } else {
            for (ChatUserEntity item : targetList) {
                if (containsByEntity(externalList, item.getChatEntity())) {
                    return item.getChatEntity();
                }
            }
        }
        return null;
    }

    private boolean containsByEntity(List<ChatUserEntity> list, ChatEntity entity) {
        if (!entity.getIsDialog()) {
            return false;
        }
        for (ChatUserEntity item : list) {
            if (item.getChatEntity().getId() == entity.getId()) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    void addChatToUser(ChatEntity chatEntity, UUID userId) {
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
}
