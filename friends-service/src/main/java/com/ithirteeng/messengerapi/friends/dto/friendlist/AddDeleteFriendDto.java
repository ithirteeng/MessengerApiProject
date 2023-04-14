package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddDeleteFriendDto {
    @NotNull(message = "Id добавляемого пользователя обязателен!")
    private UUID externalUserId;
}