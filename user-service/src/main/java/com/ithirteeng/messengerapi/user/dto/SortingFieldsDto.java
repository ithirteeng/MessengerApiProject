package com.ithirteeng.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * DTO для полей сортировки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SortingFieldsDto {

    private Sort.Direction login;

    private Sort.Direction email;

    private Sort.Direction fullName;

    private Sort.Direction birthDate;

    private Sort.Direction telephoneNumber;

    private Sort.Direction city;
}
