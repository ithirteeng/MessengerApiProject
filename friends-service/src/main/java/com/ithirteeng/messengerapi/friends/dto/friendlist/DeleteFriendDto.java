package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteFriendDto {
    @NotNull(message = "Id целевого пользователя обязателен!")
    private UUID targetUserId;

    @NotNull(message = "Id внешнего пользователя обязателен!")
    private UUID externalUserId;
}
