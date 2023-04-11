package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

/**
 * DTO для изменения данных профиля пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProfileDto {

    @Size(min = 2, message = "Поле должно содержать больше 2-х символов")
    private String fullName;

    private Date birthDate;

    private String telephoneNumber;

    private String city;

    private UUID avatarId;
}
