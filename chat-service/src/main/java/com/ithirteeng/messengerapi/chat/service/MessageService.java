package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.dto.message.SendChatMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.SendDialogueMessageDto;
import com.ithirteeng.messengerapi.chat.dto.message.ShowMessageDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import com.ithirteeng.messengerapi.chat.mapper.ChatMapper;
import com.ithirteeng.messengerapi.chat.mapper.MessageMapper;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final CommonService commonService;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    @Transactional
    public void sendMessageInDialogue(SendDialogueMessageDto sendDialogueMessageDto, UUID targetUserId) {
        if (sendDialogueMessageDto.getUserId().equals(targetUserId)) {
            throw new BadRequestException("Пользователь не может отослать сам себе сообщение!");
        }
        commonService.checkUserExisting(sendDialogueMessageDto.getUserId());
        commonService.checkIfUserInBlackList(sendDialogueMessageDto.getUserId(), targetUserId);
        commonService.checkIfUserInBlackList(targetUserId, sendDialogueMessageDto.getUserId());
        commonService.checkIfUsersAreFriends(sendDialogueMessageDto.getUserId(), targetUserId);

        var entity = createDialogue(sendDialogueMessageDto, targetUserId);

        var messageEntity = MessageMapper.sendDialogueMessageDtoToEntity(sendDialogueMessageDto, entity, targetUserId);
        messageRepository.save(messageEntity);
    }

    @Transactional
    ChatEntity createDialogue(SendDialogueMessageDto sendDialogueMessageDto, UUID targetUserId) {
        var result = checkIfDialogueCreated(sendDialogueMessageDto.getUserId(), targetUserId);
        if (result == null) {
            var entity = ChatMapper.dialogueToChatEntity();

            chatRepository.save(entity);

            addChatToUser(entity, sendDialogueMessageDto.getUserId());
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

    @Transactional
    public void sendMessageInChat(SendChatMessageDto sendChatMessageDto, UUID targetUserId) {
        var chatEntity = chatRepository.findById(sendChatMessageDto.getChatId())
                .orElseThrow(() -> new NotFoundException("Такого чата не существует!"));

        if (!chatUserRepository.existsChatUserByUserIdAndChatEntity(targetUserId, chatEntity)) {
            throw new BadRequestException("Пользователь не является участником чата!");
        } else {
            var messageEntity = MessageMapper.sendChatMessageDtoToEntity(sendChatMessageDto, chatEntity, targetUserId);
            messageRepository.save(messageEntity);

            if (chatEntity.getIsDialog()) {
                // TODO: send notification
            }
        }
    }

    @Transactional
    public List<ShowMessageDto> getMessagesList(UUID chatId, UUID targetUserId) {
        var chatEntity = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Такого чата не существует!"));

        if (!chatUserRepository.existsChatUserByUserIdAndChatEntity(targetUserId, chatEntity)) {
            throw new BadRequestException("Пользователь не является участником чата!");
        } else {
            var list = messageRepository.findAllByChatEntity(chatEntity);
            return mapEntitiesList(list);
        }
    }

    private List<ShowMessageDto> mapEntitiesList(List<MessageEntity> list) {
        ArrayList<ShowMessageDto> result = new ArrayList<>();
        for (MessageEntity entity : list) {
            var user = commonService.getUserById(entity.getAuthorId());
            result.add(MessageMapper.entityToshowMessageDto(entity, user.getFullName(), user.getAvatarId()));
        }
        return result;
    }
}
