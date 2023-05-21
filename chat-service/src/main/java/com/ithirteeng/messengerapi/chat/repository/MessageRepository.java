package com.ithirteeng.messengerapi.chat.repository;

import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с талицей сообщений
 */
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    /**
     * Метод для получения списка сообщений чата
     *
     * @param chatEntity чат типа {@link ChatEntity}
     * @return {@link List}<{@link MessageEntity}>
     */
    List<MessageEntity> findAllByChatEntity(ChatEntity chatEntity);

    /**
     * Метод получения списков сообщений по wildcard фильтру текста онных
     *
     * @param message сообщений
     * @return {@link List}<{@link MessageEntity}>
     */
    @Query("SELECT m FROM MessageEntity m WHERE m.messageText LIKE %:message%")
    List<MessageEntity> findAllByMessageTextLikeByOrderByCreationDateAsc(@Param("message") String message);
}
