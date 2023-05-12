package com.ithirteeng.messengerapi.chat.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatUserEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private UUID userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatId")
    private ChatEntity chatEntity;
}
