package com.ithirteeng.messengerapi.chat.dto.message;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutputMessageDto {

    private UUID chatId;

    private String chatName;

    private String message;

    private Date sendingDate;

    private String fileName;
}
