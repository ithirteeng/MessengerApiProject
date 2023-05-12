package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateChatDto {

    @NotNull(message = "Chat id mustn't be not null")
    private UUID chatId;

    @NotBlank(message = "У чата должно быть наименование!")
    private String chatName;

    private UUID avatarId;

    @Size(min = 1)
    private List<UUID> userIdsList;
}