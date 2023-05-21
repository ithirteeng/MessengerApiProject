package com.ithirteeng.messengerapi.chat.repository;

import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для работы с чатами
 */
@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
    /**
     * Метод для получения {@link List}<{@link ChatEntity}> из БД по wildcard фильтру fullName
     *
     * @param chatName wildcard фильтр
     * @return {@link List}<{@link ChatEntity}>
     */
    @Query("SELECT c FROM ChatEntity c WHERE lower(c.chatName) LIKE %:chatName% OR c.chatName = null")
    List<ChatEntity> findAllByChatNameLike(@Param("chatName") String chatName);
}
