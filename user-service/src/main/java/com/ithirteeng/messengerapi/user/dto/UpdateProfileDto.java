package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

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

    private String fullName;

    private Date birthDate;

    private String telephoneNumber;

    private String city;

    private UUID avatar;
}
