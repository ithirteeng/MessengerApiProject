package com.ithirteeng.messengerapi.friends.mapper;

import com.ithirteeng.messengerapi.friends.dto.friendlist.AddFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.ShortFriendDto;
import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FriendsMapper {

    public static FriendEntity createEntityFromNewFriendDto(AddFriendDto addFriendDto) {
        return FriendEntity.builder()
                .addFriendDate(new Date())
                .addingUserId(addFriendDto.getAddingUserId())
                .targetUserId(addFriendDto.getTargetUserId())
                .fullName(addFriendDto.getFullName())
                .build();
    }

    public static ShortFriendDto shortDtoFromEntity(FriendEntity friendEntity) {
        return ShortFriendDto.builder()
                .addFriendDate(friendEntity.getAddFriendDate())
                .fullName(friendEntity.getFullName())
                .deleteFriendDate(friendEntity.getDeleteFriendDate())
                .addingUserId(friendEntity.getAddingUserId())
                .build();
    }

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
