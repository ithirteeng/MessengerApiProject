package com.ithirteeng.messengerapi.notification.entity;

import com.ithirteeng.messengerapi.notification.enums.NotificationType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "type")
    private NotificationType type;

    @Column(name = "text")
    private String text;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "read_time")
    private LocalDateTime readTime;

    @Column(name = "receive_time")
    private LocalDateTime receiveTime;
}
