package com.ithirteeng.messengerapi.user.dto;

import com.ithirteeng.messengerapi.common.model.UserDto;
import lombok.*;
import org.springframework.data.domain.Sort;

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

    private Sort sortInfo;

    private int totalPages;

    private int pageNumber;

    private int pageSize;
}
