package com.ithirteeng.messengerapi.chat.repository;

import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с таблицей чат-юзер
 */
@Repository
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, UUID> {
    /**
     * Метод для проверки на существование записи по идентификатору пользователя и чату
     *
     * @param userId идентификатор пользователя
     * @param chatEntity чат типа {@link ChatEntity}
     * @return {@link Boolean}
     */
    Boolean existsChatUserByUserIdAndChatEntity(UUID userId, ChatEntity chatEntity);

    /**
     * Метод для удаления записей по чату
     *
     * @param chatEntity чат типа {@link ChatEntity}
     */
    void deleteAllByChatEntity(ChatEntity chatEntity);

    /**
     * Метод для получения записей по идентификатору пользователя
     *
     * @param userId идентификатор пользователя
     * @return {@link List}<@{@link ChatUserEntity}>
     */
    List<ChatUserEntity> findAllByUserId(UUID userId);

    /**
     * Метод для получения записей по чату
     *
     * @param chatEntity чат типа {@link ChatEntity}
     * @return {@link List}<@{@link ChatUserEntity}>
     */
    List<ChatUserEntity> findAllByChatEntity(ChatEntity chatEntity);

}
