package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * ДТО для вывода списка друзей с информацией о пагинации
 */
@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutputFriendsPageDto {
    private List<ShortFriendDto> friends;

    private Sort sortInfo;

    private int totalPages;

    private int pageNumber;

    private int pageSize;
}
