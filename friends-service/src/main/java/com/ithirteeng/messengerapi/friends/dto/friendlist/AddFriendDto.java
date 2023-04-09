package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "Id целевого пользователя обязателен!")
    private UUID targetUserId;

    @NotBlank(message = "Id добавляемого пользователя обязателен!")
    private UUID addingUserId;

    @NotBlank(message = "ФИО добавляемого пользователя обязательно!")
    private String fullName;
}