package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * ДТО для показа чата
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShowChatDto {
    private String chatName;

    private UUID chatAdmin;

    private Date creationDate;

    private UUID avatarId;
}
