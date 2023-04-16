package com.ithirteeng.messengerapi.user.dto;

import com.ithirteeng.messengerapi.user.utils.constants.RegexConstants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
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
    @Size(min = 2, message = "Поле должно содержать больше 2-х символов")
    private String login;

    @NotBlank(message = "Почта обязательна!")
    @Email(message = "Email невалиден")
    private String email;

    @NotBlank(message = "Пароль обязателен!")
    @Size(min = 4, max = 8, message = "пароль должен содержать от 4 до 8 символов")
    private String password;

    @NotBlank(message = "ФИО обязательно!")
    @Size(min = 2, message = "Поле должно содержать больше 2-х символов")
    @Pattern(regexp = RegexConstants.FULL_NAME_REGEX, message = "Имя невалидно!")
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Дата невалидна!")
    private Date birthDate;

    private String telephoneNumber;

    private String city;

    private UUID avatarId;
}
