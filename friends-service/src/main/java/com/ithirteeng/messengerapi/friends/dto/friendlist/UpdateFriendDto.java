package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateFriendDto {
    @NotBlank(message = "Id внешнего пользователя обязателен!")
    private UUID externalUserId;

    @NotBlank(message = "ФИО внешнего пользователя обязательно!")
    private String fullName;
}
