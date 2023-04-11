package com.ithirteeng.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SortingFiltersDto {
    private String login;

    private String email;

    private String fullName;

    private Date birthDate;

    private String telephoneNumber;

    private String city;
}
