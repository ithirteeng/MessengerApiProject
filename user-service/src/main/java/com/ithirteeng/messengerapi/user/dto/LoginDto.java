package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * DTO для аутентификации по лоигну и паролю
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginDto {

    @NotBlank(message = "Логин обязателен")
    private String login;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}
