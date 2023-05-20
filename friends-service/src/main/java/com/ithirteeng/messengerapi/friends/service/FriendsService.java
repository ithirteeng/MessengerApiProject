package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.common.model.UserDto;
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
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Сервис друзей
 */
@EnablePaginationService
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    private final CommonService commonService;

    private final CheckPaginationDetailsService paginationDetailsService;

    private final BlackListService blackListService;

    private final StreamBridge streamBridge;

    /**
     * Метод для получения данных о друге
     *
     * @param targetId Id целевого пользователя
     * @param friendId Id внешнего пользователя
     * @return {@link FullFriendDto}
     * @throws NotFoundException в случае, если пользователь не является другом
     * @throws ConflictException в случае, если пользователь уже был удален из друзей
     */
    @Transactional(readOnly = true)
    public FullFriendDto getFriendData(UUID targetId, UUID friendId) {
        commonService.checkUserExisting(friendId);

        var entity = friendsRepository.findByTargetUserIdAndAddingUserId(targetId, friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + friendId + " не является другом"));

        if (entity.getDeleteFriendDate() != null) {
            throw new ConflictException("Пользователь с таким id " + friendId + " удален из списка друзей");
        } else {
            return FriendsMapper.fullDtoFromEntity(entity);
        }
    }

    /**
     * Метод для добавления друга (взаимно)
     *
     * @param friendId     Id внешнего пользователя
     * @param targetUserId Id целевого пользователя
     * @throws ConflictException   в случае, если целевой или внешний пользователи находитесь в ЧС
     * @throws BadRequestException в случае, если пользователь попробует сам себя добавить в друзья
     */
    @Transactional
    public void addFriend(UUID friendId, UUID targetUserId) {
        commonService.checkUserExisting(friendId);

        if (blackListService.checkIfTargetUserInExternalUsersBlackList(targetUserId, friendId)) {
            throw new ConflictException("Вы находитесь в черном списке пользователя");
        } else if (blackListService.checkIfTargetUserInExternalUsersBlackList(friendId, targetUserId)) {
            throw new ConflictException("Пользователь находится в вашем черном списке");
        }

        if (targetUserId.equals(friendId)) {
            throw new BadRequestException("Пользователь не может добавить сам себя в друзья!");
        }

        var externalUser = commonService.getUserById(friendId);
        var targetUser = commonService.getUserById(targetUserId);

        addFriendToTargetUser(friendId, targetUser);
        addFriendToTargetUser(targetUserId, externalUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        var dto = CreateNotificationDto.builder()
                .userId(friendId)
                .text("Вас добавил в друзья в " + formattedDateTime + " пользователь с id: " + targetUserId)
                .type(NotificationType.FRIENDS_ADD)
                .build();
        sendNotification(dto);
    }

    /**
     * Впомогательный метод для добавления внешнего пользователя в друзья к целевому
     *
     * @param targetUserId    Id целевого пользователя
     * @param externalUserDto Id внешнего пользователя
     * @throws ConflictException в случае, если пользователь уже является другом
     */
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

    /**
     * Метод для взаимного удаления из друзей
     *
     * @param friendId     Id внешнего пользователя
     * @param targetUserId Id целевого пользователя
     * @throws ConflictException в случае, если пользователь попробует сам себя удалить из друзей
     * @throws NotFoundException в случае, если пользователя не будет в списке друзей
     */
    @Transactional
    public void deleteFriend(UUID friendId, UUID targetUserId) {
        commonService.checkUserExisting(friendId);

        if (targetUserId.equals(friendId)) {
            throw new ConflictException("Пользователь не может удалить сам себя в друзья!");
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            var dto = CreateNotificationDto.builder()
                    .userId(friendId)
                    .text("Вас удалил из друзей в " + formattedDateTime + " пользователь с id: " + targetUserId)
                    .type(NotificationType.FRIENDS_REMOVE)
                    .build();
            sendNotification(dto);
        }
    }

    /**
     * Метод для получение данных из БД с пагинацией
     *
     * @param sortingDto   ДТО с данными для пагинации
     * @param targetUserId Id целевого пользователя
     * @return {@link OutputFriendsPageDto}
     * @throws BadRequestException в случае, если номер страницы превыет число онных
     */
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

    /**
     * Вспомогательный метод для получения {@link Example}<{@link FriendEntity}>
     *
     * @param filtersInfo  объект {@link PageFiltersDto} с фильтрами
     * @param targetUserId Id целевого пользователя
     * @return {@link Example}<{@link FriendEntity}>
     */
    private Example<FriendEntity> setupFriendEntityExample(PageFiltersDto filtersInfo, UUID targetUserId) {
        var exampleFriend = FriendEntity.from(
                filtersInfo.getAddingDate(),
                filtersInfo.getDeletingDate(),
                filtersInfo.getExternalId(),
                targetUserId
        );
        return Example.of(exampleFriend);
    }

    /**
     * Метод для получения данных по wildcard фильтру fullName
     *
     * @param searchDto    ДТО для поиска по wildcard фильтру fullName с пагинацией
     * @param targetUserId Id целевого пользователя
     * @return {@link OutputFriendsPageDto}
     * @throws BadRequestException в случае, если номер страницы превыет число онных
     */
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

    /**
     * Метод для обновления поля fullName для всех записей в БД
     *
     * @param friendId Id внешнего пользователя
     * @throws NotFoundException в случае, если пользователя нет в друзьях
     */
    @Transactional
    public void updateFullNameFields(UUID friendId) {
        commonService.checkUserExisting(friendId);
        var user = commonService.getUserById(friendId);

//        if (!friendsRepository.existsByAddingUserIdAndTargetUserId(friendId, targetUserId)) {
//            throw new NotFoundException("Пользователя нет в ваших друзьях!");
//        }

        friendsRepository.updateFullNameByAddingUserId(friendId, user.getFullName());
    }

    /**
     * Метод для проверки, являются ли пользватели друзьями
     *
     * @param targetUserId   ID целевого юзера
     * @param externalUserId ID внешнего пользователя
     * @return {@link Boolean}
     */
    @Transactional
    public Boolean checkIfUsersAreFriends(UUID targetUserId, UUID externalUserId) {
        return friendsRepository.existsByAddingUserIdAndTargetUserId(externalUserId, targetUserId);
    }

    /**
     * Метод для отслания уведомления
     *
     * @param dto ДТО для создания уведомления
     */
    private void sendNotification(CreateNotificationDto dto) {
        streamBridge.send("notificationEvent-out-0", dto);
    }

}
