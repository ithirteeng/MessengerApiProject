package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * ДТО чата
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatDto {
    private UUID id;

    private Boolean isDialog;

    private String chatName;

    private UUID chatAdmin;

    private Date creationDate;

    private UUID avatarId;
}
