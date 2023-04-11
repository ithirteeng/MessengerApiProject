package com.ithirteeng.messengerapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InputPageDto {

    private int pageNumber;

    private int pageSize;
}
