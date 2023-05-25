package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * DTO для получения данных профиля по логину
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GetProfileDto {
    @NotBlank(message = "Поле логина не должно быть пустым!")
    private String login;
}
