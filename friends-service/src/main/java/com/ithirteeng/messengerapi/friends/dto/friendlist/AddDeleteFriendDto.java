package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * ДТО для добавления/удаления пользователя из друзей
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddDeleteFriendDto {
    @NotNull(message = "Id добавляемого пользователя обязателен!")
    private UUID externalUserId;
}