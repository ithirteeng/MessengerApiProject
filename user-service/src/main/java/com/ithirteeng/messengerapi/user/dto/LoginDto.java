package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(min = 2, max = 13, message = "Пароль должен содержать от 2-х до 13-ти символов")
    private String password;
}
