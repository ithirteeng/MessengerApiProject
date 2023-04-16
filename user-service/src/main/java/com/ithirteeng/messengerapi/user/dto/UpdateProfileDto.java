package com.ithirteeng.messengerapi.user.dto;

import com.ithirteeng.messengerapi.user.utils.constants.RegexConstants;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

/**
 * DTO для изменения данных профиля пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateProfileDto {

    @Size(min = 2, message = "Поле должно содержать больше 2-х символов")
    @Pattern(regexp = RegexConstants.FULL_NAME_REGEX, message = "Имя невалидно!")
    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @Past(message="Дата невалидна!")
    private Date birthDate;

    private String telephoneNumber;

    private String city;

    private UUID avatarId;
}
