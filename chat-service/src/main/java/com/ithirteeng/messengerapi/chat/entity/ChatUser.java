package com.ithirteeng.messengerapi.chat.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatUser {
    @Id
    private UUID userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatId", referencedColumnName = "id")
    private ChatEntity chatEntity;
}
