package com.ithirteeng.messengerapi.chat.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Entity чата
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "isDialog")
    private Boolean isDialog;

    @Column(name = "chatName")
    private String chatName;

    @Column(name = "chatAdmin")
    private UUID chatAdmin;

    @Column(name = "creationDate")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "avatarId")
    private UUID avatarId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMessageDate;

    @Column
    private UUID lastMessageId;

    @Column
    private UUID lasMessageAuthorId;

    @OneToMany(mappedBy = "chatEntity")
    private List<ChatUserEntity> chatUserEntitiesList;

    @OneToMany(mappedBy = "chatEntity")
    private List<MessageEntity> messageEntitiesList;

}
