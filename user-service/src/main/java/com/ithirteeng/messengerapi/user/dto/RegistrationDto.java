package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

/**
 * DTO для регистрации нового пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationDto {

    @NotBlank(message = "Логин обязателен!")
    private String login;

    @NotBlank(message = "Почта обязательна!")
    private String email;

    @NotBlank(message = "Пароль обязателен!")
    private String password;

    @NotBlank(message = "ФИО обязательно!")
    private String fullName;

    private Date birthDate;

    private String telephoneNumber;

    private String city;

    private UUID avatarId;
}
