package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

/**
 * ДТО для данных с пагинацией
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageInfoDto {

    private Integer pageNumber;

    private Integer pageSize;

    private Integer pagesCount;

}
