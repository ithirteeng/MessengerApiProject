package com.ithirteeng.messengerapi.chat.dto.chat;

import com.ithirteeng.messengerapi.chat.dto.common.PageInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

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

    private String chatName;
}
