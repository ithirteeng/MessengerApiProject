package com.ithirteeng.messengerapi.friends.dto.blacklist;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * ДТО для добавления/удаления пользователя из черного списка
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddDeleteNoteDto {
    @NotNull(message = "Id добавляемого пользователя обязателен!")
    private UUID externalUserId;
}