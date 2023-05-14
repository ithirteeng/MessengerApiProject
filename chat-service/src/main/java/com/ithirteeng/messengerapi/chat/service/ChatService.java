package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.dto.chat.*;
import com.ithirteeng.messengerapi.chat.dto.common.PageInfoDto;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import com.ithirteeng.messengerapi.chat.mapper.ChatMapper;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final CommonService commonService;

    private final MessageRepository messageRepository;

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

    @Transactional(readOnly = true)
    public Object getChatInfoById(UUID chatId, UUID targetUserId) {
        var entity = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Такого чата не существует!"));
        if (!chatUserRepository.existsChatUserByUserIdAndChatEntity(targetUserId, entity)) {
            throw new BadRequestException("Пользователь не является участником чата!");
        } else {
            if (entity.getIsDialog()) {
                var list = chatUserRepository.findAllByChatEntity(entity);
                for (ChatUserEntity item : list) {
                    if (!item.getUserId().equals(targetUserId)) {
                        var user = commonService.getUserById(item.getUserId());
                        return ChatMapper.chatEntityToShowDialogueDto(entity, user.getFullName());
                    }
                }
                throw new NotFoundException("Такой чат не найден");
            } else {
                return ChatMapper.chatEntityToShowChatDto(entity);
            }
        }
    }

    @Transactional
    public OutputPageChatDto getPage(InputChatPageDto inputDto, UUID targetUserId) {
        var paginationInfo = inputDto.getPageInfo();
        if (paginationInfo == null) {
            paginationInfo = new PageInfoDto(0, 50);
        }

        var correctChatsList = getUsersChats(chatUserRepository.findAllByUserId(targetUserId));
        List<ChatEntity> fullNameList = chatRepository.findAllByChatNameLike(inputDto.getChatName());

        var outputList = intersection(correctChatsList, fullNameList);
        outputList.sort(Collections.reverseOrder((o1, o2) -> {
            if (o1.getLastMessageDate() == null || o2.getLastMessageDate() == null)
                return 0;
            return o1.getLastMessageDate().compareTo(o2.getLastMessageDate());
        }));

        Integer totalPagesCount = PaginationHelperService.getTotalPagesCount(outputList, paginationInfo);
        outputList = PaginationHelperService.getCorrectPageList(outputList, paginationInfo);
        List<PageChatDto> resultList = entitiesListToPageDtoList(outputList);
        return ChatMapper.getPageFromData(resultList, paginationInfo, totalPagesCount);
    }

    private List<ChatEntity> getUsersChats(List<ChatUserEntity> idsList) {
        var result = new ArrayList<ChatEntity>();

        for (ChatUserEntity id : idsList) {
            result.add(id.getChatEntity());
        }
        return result;
    }

    public List<PageChatDto> entitiesListToPageDtoList(List<ChatEntity> list) {
        ArrayList<PageChatDto> result = new ArrayList<>();
        for (ChatEntity entity : list) {
            UUID lastMessageId = entity.getLastMessageId();
            String lastMessageText = null;
            if (lastMessageId != null) {
                MessageEntity message = messageRepository.findById(entity.getLastMessageId())
                        .orElseThrow(() -> new NotFoundException("Такого сообщения не существует!"));
                lastMessageText = message.getMessageText();
            }
            result.add(PageChatDto.builder()
                    .chatName(entity.getChatName())
                    .chatId(entity.getId())
                    .lastMessageAuthorId(entity.getLasMessageAuthorId())
                    .lastMessageDate(entity.getLastMessageDate())
                    .lastMessageText(lastMessageText)
                    .build()
            );
        }
        return result;
    }

    /**
     * Метод для пересечения двух списков
     *
     * @param list1 первый список
     * @param list2 второй список
     * @param <T>   любой тип данных
     * @return {@link List}
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>();

        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }


}
