package com.ithirteeng.messengerapi.friends.mapper;

import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.FullNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.ShortNoteDto;
import com.ithirteeng.messengerapi.friends.entity.BlockedUserEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Маппер для сущностей черного списка
 */
@Component
public class BlackListMapper {
    /**
     * Метод для преобразования объектов {@link UserDto} в {@link BlockedUserEntity}
     *
     * @param userDto      ДТО пользователя
     * @param targetUserId Id целевого пользователя
     * @return {@link BlockedUserEntity}
     */
    public static BlockedUserEntity createEntityFromUserDto(UserDto userDto, UUID targetUserId) {
        return BlockedUserEntity.builder()
                .addNoteDate(new Date())
                .addingUserId(userDto.getId())
                .targetUserId(targetUserId)
                .fullName(userDto.getFullName())
                .build();
    }

    /**
     * Метод для преобразования объектов {@link BlockedUserEntity} в {@link ShortNoteDto}
     *
     * @param blockedUserEntity Объект с данными из БД
     * @return {@link ShortNoteDto}
     */
    public static ShortNoteDto shortDtoFromEntity(BlockedUserEntity blockedUserEntity) {
        return ShortNoteDto.builder()
                .addNoteDate(blockedUserEntity.getAddNoteDate())
                .fullName(blockedUserEntity.getFullName())
                .deleteNoteDate(blockedUserEntity.getDeleteNoteDate())
                .externalUserId(blockedUserEntity.getAddingUserId())
                .build();
    }

    /**
     * Метод для преобразования объектов {@link BlockedUserEntity} в {@link FullNoteDto}
     *
     * @param blockedUserEntity Объект с данными из БД
     * @return {@link FullNoteDto}
     */
    public static FullNoteDto fullDtoFromEntity(BlockedUserEntity blockedUserEntity) {
        return FullNoteDto.builder()
                .addNoteDate(blockedUserEntity.getAddNoteDate())
                .fullName(blockedUserEntity.getFullName())
                .deleteNoteDate(blockedUserEntity.getDeleteNoteDate())
                .externalUserId(blockedUserEntity.getAddingUserId())
                .targetUserId(blockedUserEntity.getTargetUserId())
                .build();
    }
}
