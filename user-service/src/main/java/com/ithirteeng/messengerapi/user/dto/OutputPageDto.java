package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import java.util.List;

/**
 * DTO для вывода списка пользователей и информации о пагинации
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutputPageDto {

    private List<UserDto> users;

    private int totalPages;

    private int pageNumber;

    private int pageSize;
}
