package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddFriendDto {

    private Date addFriendDate;

    private Date deleteFriendDate;

    @NotNull(message = "Id целевого пользователя обязателен!")
    private UUID targetUserId;

    @NotNull(message = "Id добавляемого пользователя обязателен!")
    private UUID addingUserId;

    @NotBlank(message = "ФИО добавляемого пользователя обязательно!")
    private String fullName;
}