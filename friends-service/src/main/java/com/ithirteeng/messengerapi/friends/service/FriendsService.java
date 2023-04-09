package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.friends.dto.friendlist.AddFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.mapper.FriendsMapper;
import com.ithirteeng.messengerapi.friends.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    @Transactional(readOnly = true)
    public FullFriendDto getFriendData(UUID targetId, UUID friendId) {
        var entity = friendsRepository.getFriendEntitiesByTargetUserIdAndAddingUserId(targetId, friendId)
                .orElseThrow(() -> new NotFoundException("Друга с таким id " + friendId + " не существует"));

        if (entity.getDeleteFriendDate() != null) {
            throw new BadRequestException("Пользователь с таким id " + friendId + " удален из списка друзей");
        } else {
            return FriendsMapper.fullDtoFromEntity(entity);
        }
    }

    @Transactional
    public void addFriend(AddFriendDto addFriendDto) {
        var entity = friendsRepository.getFriendEntitiesByTargetUserIdAndAddingUserId(addFriendDto.getTargetUserId(), addFriendDto.getAddingUserId())
                .orElse(null);
        if (entity != null && entity.getDeleteFriendDate() != null) {
            entity.setDeleteFriendDate(null);
            entity.setAddFriendDate(new Date());
            entity.setFullName(addFriendDto.getFullName());
            friendsRepository.save(entity);
        } else if (entity == null) {
            friendsRepository.save(FriendsMapper.createEntityFromNewFriendDto(addFriendDto));
        } else {
            throw new ConflictException("Такой друг уже существует!");
        }
    }


}
