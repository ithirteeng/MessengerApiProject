package com.ithirteeng.messengerapi.friends.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;
import java.util.UUID;

/**
 * ДТО для фильтров в запросах с пагинацией
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PageFiltersDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @Past(message="Date invalid!")
    private Date addingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @Past(message="Date invalid!")
    private Date deletingDate;

    private UUID externalId;

    private String fullName;

}
