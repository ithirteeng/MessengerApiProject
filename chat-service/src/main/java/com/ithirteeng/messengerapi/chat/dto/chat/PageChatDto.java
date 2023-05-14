package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageChatDto {
    private UUID chatId;

    private String chatName;

    private String lastMessageText;

    private Date lastMessageDate;

    private UUID lastMessageAuthorId;
}
