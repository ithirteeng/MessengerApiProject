package com.ithirteeng.messengerapi.chat.repository;

import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> findAllByChatEntity(ChatEntity chatEntity);

    List<MessageEntity> findAllByChatEntityOrderByCreationDateDesc(ChatEntity chatEntity);

    @Query("SELECT m FROM MessageEntity m WHERE m.messageText LIKE %:message%")
    List<MessageEntity> findAllByMessageTextLikeByOrderByCreationDateAsc(@Param("message") String message);
}
