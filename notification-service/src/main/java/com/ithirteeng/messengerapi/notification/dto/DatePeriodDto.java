package com.ithirteeng.messengerapi.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * ДТО для периода создания уведолений
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatePeriodDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime fromDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime toDateTime;

}
