package com.ithirteeng.messengerapi.chat.dto.file;

import lombok.*;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ShowFileDto {
    private String storageFileId;

    private String name;

    private String fileSize;
}
