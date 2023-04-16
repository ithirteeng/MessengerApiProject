package com.ithirteeng.messengerapi.friends.dto.friendlist;

import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * ДТО для вывода полноый информации о друге
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FullFriendDto {

    private Date addFriendDate;

    private Date deleteFriendDate;

    private UUID targetUserId;

    private UUID addingUserId;

    private String fullName;
}
