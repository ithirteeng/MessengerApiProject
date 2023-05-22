package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.dto.message.*;
import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import com.ithirteeng.messengerapi.chat.mapper.ChatMapper;
import com.ithirteeng.messengerapi.chat.mapper.MessageMapper;
import com.ithirteeng.messengerapi.chat.repository.ChatRepository;
import com.ithirteeng.messengerapi.chat.repository.ChatUserRepository;
import com.ithirteeng.messengerapi.chat.repository.MessageRepository;
import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с сообщениями
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final CommonService commonService;

    private final ChatUserRepository chatUserRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    private final StreamBridge streamBridge;

    private final FileService fileService;

    /**
     * Метод для отправки сообщения в диалог, если сообщение - первое, то еще и создается диалог
     *
     * @param sendDialogueMessageDto ДТО ({@link SendDialogueMessageDto}) для отправки сообщения
     * @param targetUserId           идентификатор целевого пользователя
     * @throws BadRequestException в случае попытки отослать самому себе сообщение
     */
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

        var filesList = sendDialogueMessageDto.getFilesIdsList();
        checkFilesExisting(filesList);

        var messageEntity = MessageMapper.sendDialogueMessageDtoToEntity(sendDialogueMessageDto, entity, targetUserId);
        messageRepository.save(messageEntity);

        fileService.attachFilesToMessage(filesList, messageEntity.getId());

        entity.setLastMessageId(messageEntity.getId());
        entity.setLastMessageDate(messageEntity.getCreationDate());
        entity.setLasMessageAuthorId(messageEntity.getAuthorId());

        chatRepository.save(entity);
        sendNotificationToUser(sendDialogueMessageDto.getUserId(), sendDialogueMessageDto.getMessage(), targetUserId);
    }

    /**
     * Метод для отправки уведомления внешнему пользователю о сообщении
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @param message        сообщение
     * @param targetUserId   идентификатор уелевого пользователя
     */
    private void sendNotificationToUser(UUID externalUserId, String message, UUID targetUserId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        var userData = commonService.getUserById(targetUserId);
        var dto = CreateNotificationDto.builder()
                .userId(externalUserId)
                .text("Новое сообщение от пользователя с id " + targetUserId
                        + " и ФИО: " + userData.getFullName() + ". Время отправки сообщения: "
                        + formattedDateTime + ". Сообщение: '"
                        + message.substring(0, Math.min(message.length(), 100))
                        + "'")
                .type(NotificationType.MESSAGE)
                .build();
        sendNotification(dto);
    }

    /**
     * Методя для создания диалога
     *
     * @param sendDialogueMessageDto ДТО ({@link SendDialogueMessageDto}) для отправки сообщения
     * @param targetUserId           идентификатор целевого пользователя
     * @return {@link ChatEntity}
     */
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

    /**
     * Метод для проверки, был ли уже создан диалог
     *
     * @param externalUserId идентификатор внешнего пользователя
     * @param targetUserId   идентификатор целевого пользователя
     * @return {@link ChatEntity}
     */
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

    /**
     * Метод для проверки сущестования записи чат-юзер по чату
     *
     * @param list   список с объектами типа чат-юзер {@link ChatUserEntity}
     * @param entity {@link ChatEntity} чат
     * @return {@link Boolean}
     */
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

    /**
     * Метод для добавления в БД запись типа чат-юзер
     *
     * @param chatEntity объект чата {@link ChatEntity}
     * @param userId     идентификатор юзера
     */
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

    /**
     * Метод для отправки сообщения в чат
     *
     * @param sendChatMessageDto ДТО ({@link SendChatMessageDto}) для отправки сообщения в чат
     * @param targetUserId       идентификатор целевого пользователя
     * @throws NotFoundException   в случае несуществования чата
     * @throws BadRequestException в случае когда пользователь не является участником чата
     */
    @Transactional
    public void sendMessageInChat(SendChatMessageDto sendChatMessageDto, UUID targetUserId) {
        var chatEntity = chatRepository.findById(sendChatMessageDto.getChatId())
                .orElseThrow(() -> new NotFoundException("Такого чата не существует!"));

        if (!chatUserRepository.existsChatUserByUserIdAndChatEntity(targetUserId, chatEntity)) {
            throw new BadRequestException("Пользователь не является участником чата!");
        } else {

            var filesList = sendChatMessageDto.getFilesIdsList();
            checkFilesExisting(filesList);

            var messageEntity = MessageMapper.sendChatMessageDtoToEntity(sendChatMessageDto, chatEntity, targetUserId);
            messageRepository.save(messageEntity);

            chatEntity.setLastMessageId(messageEntity.getId());
            chatEntity.setLastMessageDate(messageEntity.getCreationDate());
            chatEntity.setLasMessageAuthorId(messageEntity.getAuthorId());

            fileService.attachFilesToMessage(filesList, messageEntity.getId());

            chatRepository.save(chatEntity);

            if (chatEntity.getIsDialog()) {
                List<ChatUserEntity> data = chatUserRepository.findAllByChatEntity(chatEntity);
                UUID externalUserId;
                if (!data.get(0).getUserId().equals(targetUserId)) {
                    externalUserId = data.get(0).getUserId();
                } else {
                    externalUserId = data.get(1).getUserId();
                }
                sendNotificationToUser(externalUserId, sendChatMessageDto.getMessage(), targetUserId);
            }
        }
    }

    /**
     * Метод для получения списка сообщений
     *
     * @param chatId       идентификатор чата
     * @param targetUserId идентификатор юзера
     * @return {@link List}<{@link ShowMessageDto}>
     * @throws NotFoundException   в случае несуществования чата
     * @throws BadRequestException в случае, когда пользователь не явлется участником чата
     */
    @Transactional
    public List<ShowMessageDto> getMessagesList(UUID chatId, UUID targetUserId) {
        var chatEntity = chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Такого чата не существует!"));

        if (!chatUserRepository.existsChatUserByUserIdAndChatEntity(targetUserId, chatEntity)) {
            throw new BadRequestException("Пользователь не является участником чата!");
        } else {
            var list = messageRepository.findAllByChatEntityOrderByCreationDateDesc(chatEntity);
            return mapEntitiesList(list);
        }
    }

    /**
     * Метод для преобразования списка объектов типа {@link MessageEntity} в список объектов {@link ShowMessageDto}
     *
     * @param list список объектов типа {@link MessageEntity}
     * @return {@link List}<{@link ShowMessageDto}>
     */
    private List<ShowMessageDto> mapEntitiesList(List<MessageEntity> list) {
        ArrayList<ShowMessageDto> result = new ArrayList<>();
        for (MessageEntity entity : list) {
            var user = commonService.getUserById(entity.getAuthorId());
            result.add(MessageMapper.entityToshowMessageDto(entity, user.getFullName(), user.getAvatarId()));
        }
        return result;
    }

    /**
     * Метод для получения сообщений по тексту в порядке убывания по дате последнего сообщения
     *
     * @param findMessageDto ДТО ({@link FindMessageDto}) с фильтрами для поиска сообщений
     * @param targetUserId   идентификатор пользователя
     * @return {@link List}<{@link OutputMessageDto}>
     */
    @Transactional
    public List<OutputMessageDto> getMessagesByText(FindMessageDto findMessageDto, UUID targetUserId) {
        List<ChatUserEntity> chatsList = chatUserRepository.findAllByUserId(targetUserId);
        List<MessageEntity> messagesList = messageRepository.findAllByMessageTextLikeByOrderByCreationDateAsc(findMessageDto.getMessage());

        ArrayList<OutputMessageDto> resultList = new ArrayList<>();

        for (MessageEntity entity : messagesList) {
            if (ifListContainsId(chatsList, entity.getChatEntity().getId())) {
                String chatName = getChatName(entity, targetUserId);
                resultList.add(MessageMapper.entityToOutputMessageDto(entity, chatName, "test"));
            }
        }

        resultList.sort(Collections.reverseOrder((o1, o2) -> {
            if (o1.getSendingDate() == null || o2.getSendingDate() == null)
                return 0;
            return o1.getSendingDate().compareTo(o2.getSendingDate());
        }));

        return resultList;
    }

    /**
     * Метод для получения имени чата/диалога
     *
     * @param entity       Entity сообщения ({@link MessageEntity})
     * @param targetUserId идентификатор пользователя
     * @return имя чата
     */
    private String getChatName(MessageEntity entity, UUID targetUserId) {
        var chatName = entity.getChatEntity().getChatName();
        if (entity.getChatEntity().getIsDialog()) {
            List<ChatUserEntity> idsList = chatUserRepository.findAllByChatEntity(entity.getChatEntity());
            UUID userId = idsList.get(0).getUserId();
            if (idsList.get(0).getUserId().equals(targetUserId)) {
                userId = idsList.get(1).getUserId();
            }
            var user = commonService.getUserById(userId);
            chatName = user.getFullName();
        }
        return chatName;
    }

    /**
     * Метод для проверки, содержится ли в списке объектов чат-юзера идентификатор чата
     *
     * @param list   {@link List}<{@link ChatUserEntity}> список
     * @param chatId идентиикатор чата
     * @return {@link Boolean}
     */
    private Boolean ifListContainsId(List<ChatUserEntity> list, UUID chatId) {
        for (ChatUserEntity item : list) {
            if (item.getChatEntity().getId().equals(chatId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Метод для проверки файлов на существование
     *
     * @param fileIdsList список идентифивторов файлов
     * @throws NotFoundException в случае, если файла нет в хранилище
     */
    private void checkFilesExisting(List<UUID> fileIdsList) {
        for (UUID id : fileIdsList) {
            if (!commonService.checkIfFileExists(id.toString())) {
                throw new NotFoundException("Файла с таким id: " + id + " не существует");
            }
        }
    }

    /**
     * Метод для отслания уведомления через {@link StreamBridge}
     *
     * @param dto ДТО для создания уведомления
     */
    private void sendNotification(CreateNotificationDto dto) {
        streamBridge.send("notificationEvent-out-0", dto);
    }

}
