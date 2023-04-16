package com.ithirteeng.messengerapi.friends.dto.friendlist;


import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * ДТО для вывода неполной информации о друге
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShortFriendDto {

    private Date addFriendDate;

    private Date deleteFriendDate;

    private UUID addingUserId;

    private String fullName;
}
