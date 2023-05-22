package com.ithirteeng.messengerapi.common.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FileDataDto {
    private String fileId;

    private String fileName;
}
