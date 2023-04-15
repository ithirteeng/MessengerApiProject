package com.ithirteeng.messengerapi.friends.mapper;

import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.FullNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.ShortNoteDto;
import com.ithirteeng.messengerapi.friends.entity.BlockedUserEntity;

import java.util.Date;
import java.util.UUID;

public class BlackListMapper {
    public static BlockedUserEntity createEntityFromUserDto(UserDto userDto, UUID targetUserId) {
        return BlockedUserEntity.builder()
                .addNoteDate(new Date())
                .addingUserId(userDto.getId())
                .targetUserId(targetUserId)
                .fullName(userDto.getFullName())
                .build();
    }

    public static ShortNoteDto shortDtoFromEntity(BlockedUserEntity friendEntity) {
        return ShortNoteDto.builder()
                .addNoteDate(friendEntity.getAddNoteDate())
                .fullName(friendEntity.getFullName())
                .deleteNoteDate(friendEntity.getDeleteNoteDate())
                .externalUserId(friendEntity.getAddingUserId())
                .build();
    }

    public static FullNoteDto fullDtoFromEntity(BlockedUserEntity friendEntity) {
        return FullNoteDto.builder()
                .addNoteDate(friendEntity.getAddNoteDate())
                .fullName(friendEntity.getFullName())
                .deleteNoteDate(friendEntity.getDeleteNoteDate())
                .externalUserId(friendEntity.getAddingUserId())
                .targetUserId(friendEntity.getTargetUserId())
                .build();
    }
}
