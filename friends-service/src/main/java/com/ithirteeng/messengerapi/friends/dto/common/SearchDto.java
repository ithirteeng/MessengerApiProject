package com.ithirteeng.messengerapi.friends.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchDto {
    @NotNull(message = "Информация о пагинации обязательна!")
    @Valid
    private InputPageDto pageInfo;

    private String filterName;
}
