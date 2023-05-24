package com.ithirteeng.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * DTO для получения данных профиля по логину
 */
@Data
@Getter
public class GetProfileDto {
    @NotBlank(message = "Поле логина не должно быть пустым!")
    private String login;
}
