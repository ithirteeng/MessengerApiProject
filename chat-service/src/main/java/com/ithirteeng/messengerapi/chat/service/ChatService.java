package com.ithirteeng.messengerapi.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Transactional
    public void sendMessage(UUID externalUserId, UUID targetUserId) {

    }

    @Transactional
    public void addChatToUser(UUID chatId, UUID userId) {

    }
}
