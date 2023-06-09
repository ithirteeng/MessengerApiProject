package com.ithirteeng.messengerapi.friends.dto.blacklist;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * ДТО для вывода списка записей с информацией о пагинации
 */
@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutputNotesPageDto {

    private List<ShortNoteDto> notes;

    private Sort sortInfo;

    private int totalPages;

    private int pageNumber;

    private int pageSize;
}
