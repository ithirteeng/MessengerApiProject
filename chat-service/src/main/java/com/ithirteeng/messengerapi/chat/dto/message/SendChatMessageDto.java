package com.ithirteeng.messengerapi.chat.dto.message;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * ДТО для отправки сообщения в чат
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendChatMessageDto {

    @NotNull(message = "ID чата обязателен!")
    private UUID chatId;

    @NotEmpty(message = "Сообщение не должно быть пустым")
    private String message;

    private List<UUID> filesIdsList;

}
