package com.ithirteeng.messengerapi.friends.dto.blacklist;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FullNoteDto {

    private Date addNoteDate;

    private Date deleteNoteDate;

    private UUID targetUserId;

    private UUID externalUserId;

    private String fullName;
}
