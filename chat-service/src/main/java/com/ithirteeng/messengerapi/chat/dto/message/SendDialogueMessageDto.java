package com.ithirteeng.messengerapi.chat.dto.message;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendDialogueMessageDto {

    private UUID UserId;

    @NotEmpty(message = "Сообщение не должно быть пустым")
    private String message;

    private List<UUID> filesIdsList;
}
