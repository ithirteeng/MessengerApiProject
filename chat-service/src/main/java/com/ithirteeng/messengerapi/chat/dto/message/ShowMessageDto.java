package com.ithirteeng.messengerapi.chat.dto.message;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShowMessageDto {

    private UUID messageId;

    private Date sendingTime;

    private String message;

    private String senderName;

    private UUID avatarId;
}
