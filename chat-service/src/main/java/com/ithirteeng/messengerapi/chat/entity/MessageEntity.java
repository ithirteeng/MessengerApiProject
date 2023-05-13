package com.ithirteeng.messengerapi.chat.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @Column(name = "creationDate")
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "messageText")
    @Length(max = 500)
    private String messageText;

    @Column(name = "authorId")
    private UUID authorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chatId")
    private ChatEntity chatEntity;
}

