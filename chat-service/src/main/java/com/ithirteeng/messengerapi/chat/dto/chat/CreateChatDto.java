package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * ДТО для создания чата
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateChatDto {

    @NotBlank(message = "У чата должно быть наименование!")
    private String chatName;

    private UUID avatarId;

    @Size(min = 1, message = "Размер списка не может быть меньше 1")
    @NotNull(message = "Список не может быть null")
    private List<UUID> usersIdsList;

}
