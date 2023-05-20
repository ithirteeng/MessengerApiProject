package com.ithirteeng.messengerapi.chat.dto.message;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindMessageDto {

    @NotNull
    private String message;
}
