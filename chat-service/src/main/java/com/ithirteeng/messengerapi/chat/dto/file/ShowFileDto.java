package com.ithirteeng.messengerapi.chat.dto.file;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ShowFileDto {

    private UUID id;

    private String storageFileId;

    private String name;

    private String fileSize;
}
