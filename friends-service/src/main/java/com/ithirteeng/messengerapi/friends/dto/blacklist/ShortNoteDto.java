package com.ithirteeng.messengerapi.friends.dto.blacklist;


import lombok.*;

import java.util.Date;
import java.util.UUID;

/**
 * ДТО для вывода неполных данных о записи
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShortNoteDto {

    private Date addNoteDate;

    private Date deleteNoteDate;

    private UUID externalUserId;

    private String fullName;
}
