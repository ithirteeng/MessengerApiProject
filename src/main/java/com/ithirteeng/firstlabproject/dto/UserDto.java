package com.ithirteeng.firstlabproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;

    private String login;

    private String password;

    private String name;

    private String surname;

    private String patronymic;

    private Date birthDate;

    private Date registrationDate;
}
