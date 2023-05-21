package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import java.util.List;

/**
 * ДТО для показа данных с пагинацией
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutputPageChatDto {
    private List<PageChatDto> chats;

    private int totalPages;

    private int pageNumber;

    private int pageSize;
}
