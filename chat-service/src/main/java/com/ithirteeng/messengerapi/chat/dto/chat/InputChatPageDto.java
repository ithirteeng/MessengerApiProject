package com.ithirteeng.messengerapi.chat.dto.chat;

import com.ithirteeng.messengerapi.chat.dto.common.PageInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * ДТО для поиска по имени с пагинацией
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class InputChatPageDto {

    @Valid
    private PageInfoDto pageInfo;

    @NotNull(message = "Название чата не должно быть null!")
    private String chatName;
}
