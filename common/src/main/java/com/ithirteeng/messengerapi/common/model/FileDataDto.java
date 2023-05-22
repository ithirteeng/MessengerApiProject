package com.ithirteeng.messengerapi.common.model;

import lombok.*;

/**
 * ДТО для показа данных о загруженном файле
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FileDataDto {

    private String fileId;

    private String fileName;

    private Integer fileSize;
}
