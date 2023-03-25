package com.ithirteeng.firstlabproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {

    private String login;

    private String name;

    private String surname;

    private String patronymic;

    private Date birthDate;
}
