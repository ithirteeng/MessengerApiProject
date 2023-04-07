package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * DTO для показа даннных пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    private UUID id;

    private Date registrationDate;

    private String login;

    private String email;

    private String fullName;

    private Date birthDate;

    private String telephoneNumber;

    private String city;

    private UUID avatarId;
}
