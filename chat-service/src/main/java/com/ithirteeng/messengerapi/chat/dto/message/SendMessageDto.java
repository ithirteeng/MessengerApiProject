package com.ithirteeng.messengerapi.chat.dto.message;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendMessageDto {

    @NotNull(message = "User id mustn't be not null")
    private UUID UserId;

    @NotEmpty(message = "Сообщение не должно быть пустым")
    private String message;

    private List<UUID> filesIdsList;
}
