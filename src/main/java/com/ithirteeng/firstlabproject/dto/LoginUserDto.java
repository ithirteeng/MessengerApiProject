package com.ithirteeng.firstlabproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {

    @NotNull
    private String login;
    @NotNull
    private String password;
}
