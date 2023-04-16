package com.ithirteeng.messengerapi.friends.mapper;

import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.ShortFriendDto;
import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Маппер для сущностей друзей
 */
@Component
public class FriendsMapper {

    /**
     * Метод для преобразования объектов {@link UserDto} в {@link FriendEntity}
     *
     * @param userDto      ДТО пользователя
     * @param targetUserId Id целевого пользователя
     * @return {@link FriendEntity}
     */
    public static FriendEntity createEntityFromNewFriendDto(UserDto userDto, UUID targetUserId) {
        return FriendEntity.builder()
                .addFriendDate(new Date())
                .addingUserId(userDto.getId())
                .targetUserId(targetUserId)
                .fullName(userDto.getFullName())
                .build();
    }

    /**
     * Метод для преобразования объектов {@link FriendEntity} в {@link ShortFriendDto}
     *
     * @param friendEntity Объект с данными из БД
     * @return {@link ShortFriendDto}
     */
    public static ShortFriendDto shortDtoFromEntity(FriendEntity friendEntity) {
        return ShortFriendDto.builder()
                .addFriendDate(friendEntity.getAddFriendDate())
                .fullName(friendEntity.getFullName())
                .deleteFriendDate(friendEntity.getDeleteFriendDate())
                .addingUserId(friendEntity.getAddingUserId())
                .build();
    }

    /**
     * Метод для преобразования объектов {@link FriendEntity} в {@link FullFriendDto}
     *
     * @param friendEntity Объект с данными из БД
     * @return {@link FullFriendDto}
     */
    public static FullFriendDto fullDtoFromEntity(FriendEntity friendEntity) {
        return FullFriendDto.builder()
                .addFriendDate(friendEntity.getAddFriendDate())
                .fullName(friendEntity.getFullName())
                .deleteFriendDate(friendEntity.getDeleteFriendDate())
                .addingUserId(friendEntity.getAddingUserId())
                .targetUserId(friendEntity.getTargetUserId())
                .build();
    }
}
