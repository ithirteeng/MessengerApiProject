package com.ithirteeng.messengerapi.chat.dto.chat;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShowDialogueDto {
    private String chatName;

    private Date creationDate;
}
