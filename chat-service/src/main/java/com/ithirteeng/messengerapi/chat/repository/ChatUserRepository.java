package com.ithirteeng.messengerapi.chat.repository;

import com.ithirteeng.messengerapi.chat.entity.ChatEntity;
import com.ithirteeng.messengerapi.chat.entity.ChatUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, UUID> {
    Boolean existsChatUserByUserIdAndChatEntity(UUID userId, ChatEntity chatEntity);

    void deleteAllByChatEntity(ChatEntity chatEntity);

    List<ChatUserEntity> findAllByUserId(UUID userId);

    List<ChatUserEntity> findAllByChatEntity(ChatEntity chatEntity);

}
