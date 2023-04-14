package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.consts.RequestsConstants;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.common.service.EnablePaginationService;
import com.ithirteeng.messengerapi.friends.dto.common.PageFiltersDto;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.FullFriendDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.OutputFriendsPageDto;
import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import com.ithirteeng.messengerapi.friends.mapper.FriendsMapper;
import com.ithirteeng.messengerapi.friends.mapper.PageMapper;
import com.ithirteeng.messengerapi.friends.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnablePaginationService
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final SecurityProps securityProps;

    private final CheckPaginationDetailsService paginationDetailsService;

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

        if (targetUserId == friendId) {
            throw new BadRequestException("Пользователь не может добавить сам себя в друзья!");
        }

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

        if (targetUserId == friendId) {
            throw new BadRequestException("Пользователь не может удалить сам себя в друзья!");
        }

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

    @Transactional(readOnly = true)
    public OutputFriendsPageDto getFriendsPage(SortingDto sortingDto, UUID targetUserId) {
        var pageInfo = sortingDto.getPageInfo();
        paginationDetailsService.checkPagination(pageInfo.getPageNumber(), pageInfo.getPageSize());

        var filtersInfo = sortingDto.getFilters();
        Example<FriendEntity> example = setupFriendEntityExample(filtersInfo, targetUserId);

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());
        Page<FriendEntity> friendsPage = friendsRepository.findAll(example, pageable);

        var nameFilter = filtersInfo.getFullName() == null ? "" : filtersInfo.getFullName();
        List<FriendEntity> fullNameList = friendsRepository.findByFullNameLikeAndTargetUserId(nameFilter, targetUserId);

        if (friendsPage.getTotalPages() <= pageInfo.getPageNumber() && friendsPage.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных - 1");
        }

        return PageMapper.pageToOutputPageDto(friendsPage, fullNameList);
    }

    private Example<FriendEntity> setupFriendEntityExample(PageFiltersDto filtersInfo, UUID targetUserId) {
        var exampleFriend = FriendEntity.from(
                filtersInfo.getAddingDate(),
                filtersInfo.getDeletingDate(),
                filtersInfo.getExternalId(),
                targetUserId
        );
        return Example.of(exampleFriend);
    }

    @Transactional(readOnly = true)
    public OutputFriendsPageDto searchFriends(SearchDto searchDto, UUID targetUserId) {
        var pageInfo = searchDto.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());

        Page<FriendEntity> friendsPage = friendsRepository.findAllByTargetUserId(targetUserId, pageable);
        var nameFilter = searchDto.getFilterName() == null ? "" : searchDto.getFilterName();
        List<FriendEntity> fullNameList = friendsRepository.findByFullNameLikeAndTargetUserId(nameFilter, targetUserId);

        if (friendsPage.getTotalPages() <= pageInfo.getPageNumber() && friendsPage.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных - 1");
        }

        return PageMapper.pageToOutputPageDto(friendsPage, fullNameList);
    }


}
