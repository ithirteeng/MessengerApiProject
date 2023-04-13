package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.mapper.FriendsMapper;
import com.ithirteeng.messengerapi.friends.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final SecurityProps securityProps;

    private HttpEntity<Void> setupRequestHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestsConstants.API_KEY_HEADER, securityProps.getIntegrations().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private void checkUserExisting(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1301/integration/users/check/" + userId.toString();

        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), Boolean.class
        );

        if (Boolean.FALSE.equals(responseEntity.getBody())) {
            throw new NotFoundException("Пользователя с id: " + userId + " не существует!");
        }
    }

    private UserDto getUserById(UUID userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:1301/integration/users/data/" + userId.toString();

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, setupRequestHttpEntity(), UserDto.class
        );

        return responseEntity.getBody();
    }

    @Transactional(readOnly = true)
    public FullFriendDto getFriendData(UUID targetId, UUID friendId) {
        checkUserExisting(targetId);
        checkUserExisting(friendId);

        var entity = friendsRepository.findByTargetUserIdAndAddingUserId(targetId, friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не является другом"));

        if (entity.getDeleteFriendDate() != null) {
            throw new BadRequestException("Пользователь с таким id " + friendId + " удален из списка друзей");
        } else {
            return FriendsMapper.fullDtoFromEntity(entity);
        }
    }

    @Transactional
    public void addFriend(UUID friendId, UUID targetUserId) {
        checkUserExisting(targetUserId);

        var externalUser = getUserById(friendId);
        var targetUser = getUserById(targetUserId);

        addFriendToTargetUser(friendId, targetUser);
        addFriendToTargetUser(targetUserId, externalUser);
    }

    private void addFriendToTargetUser(UUID targetUserId, UserDto externalUserDto) {
        var entity = friendsRepository.findByTargetUserIdAndAddingUserId(
                targetUserId,
                externalUserDto.getId()
        ).orElse(null);

        if (entity != null && entity.getDeleteFriendDate() != null) {
            entity.setDeleteFriendDate(null);
            entity.setAddFriendDate(new Date());
            entity.setFullName(externalUserDto.getFullName());
            friendsRepository.save(entity);
        } else if (entity == null) {
            friendsRepository.save(FriendsMapper.createEntityFromNewFriendDto(externalUserDto, targetUserId));
        } else {
            throw new ConflictException("Пользователь уже являлется вашим другом!");
        }
    }

    @Transactional
    public void deleteFriend(UUID friendId, UUID targetUserId) {
        checkUserExisting(friendId);

        var entity = friendsRepository.findByTargetUserIdAndAddingUserId(targetUserId, friendId)
                .orElseThrow(() -> new NotFoundException("Пользователя нет в друзьях!"));
        var entity2 = friendsRepository.findByTargetUserIdAndAddingUserId(friendId, targetUserId)
                .orElseThrow(() -> new NotFoundException("Пользователя нет в друзьях!"));

        if (entity.getDeleteFriendDate() != null) {
            throw new ConflictException("Пользователь с таким id уже удален из списка друзей!");
        } else {
            var date = new Date();
            entity.setDeleteFriendDate(date);
            entity2.setDeleteFriendDate(date);

            friendsRepository.save(entity);
            friendsRepository.save(entity2);
        }
    }


}
