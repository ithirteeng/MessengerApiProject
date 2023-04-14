package com.ithirteeng.messengerapi.user.dto;

import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.user.utils.enums.SortingType;
import lombok.*;

import java.util.List;

/**
 * DTO для получения данных о пользователях
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsersListDto {

    private Integer pageNumber;

    private Integer pageSize;

    private Integer pagesCount;

    private List<SortingType> sortingFilters;

    private List<UserDto> usersList;

}
