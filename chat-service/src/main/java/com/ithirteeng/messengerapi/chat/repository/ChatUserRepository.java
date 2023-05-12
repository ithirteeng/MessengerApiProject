package com.ithirteeng.messengerapi.chat.repository;

import com.ithirteeng.messengerapi.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
}
