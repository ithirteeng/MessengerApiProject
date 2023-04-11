package com.ithirteeng.messengerapi.friends.dto.friendlist;


import lombok.*;

import java.util.Date;
import java.util.UUID;

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
