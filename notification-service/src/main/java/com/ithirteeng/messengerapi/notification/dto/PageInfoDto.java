package com.ithirteeng.messengerapi.notification.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageInfoDto {

    private Integer pageNumber;

    private Integer pageSize;

}
