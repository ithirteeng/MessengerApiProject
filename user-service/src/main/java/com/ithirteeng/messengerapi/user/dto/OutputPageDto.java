package com.ithirteeng.messengerapi.user.dto;

import lombok.*;

import java.util.List;

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
