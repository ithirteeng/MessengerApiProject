package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.common.service.EnablePaginationService;
import com.ithirteeng.messengerapi.friends.dto.blacklist.FullNoteDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.OutputNotesPageDto;
import com.ithirteeng.messengerapi.friends.dto.common.PageFiltersDto;
import com.ithirteeng.messengerapi.friends.dto.common.SearchDto;
import com.ithirteeng.messengerapi.friends.dto.common.SortingDto;
import com.ithirteeng.messengerapi.friends.entity.BlockedUserEntity;
import com.ithirteeng.messengerapi.friends.mapper.BlackListMapper;
import com.ithirteeng.messengerapi.friends.mapper.PageMapper;
import com.ithirteeng.messengerapi.friends.repository.BlackListRepository;
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
 * Сервис черного списка
 */
@EnablePaginationService
@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    private final FriendsRepository friendsRepository;

    private final CommonService commonService;

    private final CheckPaginationDetailsService paginationDetailsService;

    private final StreamBridge streamBridge;

    /**
     * Метод для получение данных о записи в ЧС
     *
     * @param targetId       Id целевого пользователя
     * @param externalUserId Id внешнего пользователя
     * @return {@link FullNoteDto}
     * @throws ConflictException в том случае, если внешний пользователь уже удален из ЧС
     * @throws NotFoundException в том случае, если внешний пользователь не был в ЧС
     */
    @Transactional(readOnly = true)
    public FullNoteDto getNoteData(UUID targetId, UUID externalUserId) {
        commonService.checkUserExisting(externalUserId);

        var entity = blackListRepository.findByTargetUserIdAndAddingUserId(targetId, externalUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + externalUserId + " не находится в черном списке"));

        if (entity.getDeleteNoteDate() != null) {
            throw new ConflictException("Пользователь с id: " + externalUserId + " удален из черного списка");
        } else {
            return BlackListMapper.fullDtoFromEntity(entity);
        }
    }

    /**
     * Метод для добавления записи в ЧС
     *
     * @param externalUserId Id внешнего пользователя
     * @param targetUserId   Id целевого пользователя
     * @throws BadRequestException в случае, если пользователь захочет сам себя добавить в ЧС
     * @throws RuntimeException    в случае, если при удалении друга вылетит что-то кроме {@link ConflictException} и {@link NotFoundException}
     */
    @Transactional
    public void addNote(UUID externalUserId, UUID targetUserId) {
        commonService.checkUserExisting(externalUserId);

        if (targetUserId.equals(externalUserId)) {
            throw new BadRequestException("Пользователь не может добавить сам себя в черный список!");
        }
        var externalUser = commonService.getUserById(externalUserId);

        addNoteToTargetUser(targetUserId, externalUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        var dto = CreateNotificationDto.builder()
                .userId(externalUserId)
                .text("Вас добавил в ЧС в " + formattedDateTime + " пользователь с id: " + targetUserId)
                .type(NotificationType.BLACKLIST_ADD)
                .build();
        sendNotification(dto);
        try {
            deleteFriend(externalUserId, targetUserId);
        } catch (Exception e) {
            if (!(e instanceof ConflictException || e instanceof NotFoundException)) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Методя для удаления пользователя из списка друзей
     *
     * @param friendId     Id внешнего пользователя
     * @param targetUserId Id целевого пользователя
     * @throws ConflictException в случае, если пользователь уже удален из списка друзей
     * @throws NotFoundException в случае, если пользователя нет в друзьях
     */
    private void deleteFriend(UUID friendId, UUID targetUserId) {
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

    /**
     * Методя для добавления записи в ЧС целевого пользователя
     *
     * @param targetUserId    Id целевого пользователя
     * @param externalUserDto {@link UserDto} внешнего пользователя
     * @throws ConflictException в том случае, если пользователь уже находится в ЧС
     */
    private void addNoteToTargetUser(UUID targetUserId, UserDto externalUserDto) {
        var entity = blackListRepository.findByTargetUserIdAndAddingUserId(
                targetUserId,
                externalUserDto.getId()
        ).orElse(null);

        if (entity != null && entity.getDeleteNoteDate() != null) {
            entity.setDeleteNoteDate(null);
            entity.setAddNoteDate(new Date());
            entity.setFullName(externalUserDto.getFullName());
            blackListRepository.save(entity);
        } else if (entity == null) {
            blackListRepository.save(BlackListMapper.createEntityFromUserDto(externalUserDto, targetUserId));
        } else {
            throw new ConflictException("Пользователь уже находится в черном списке!");
        }
    }

    /**
     * Метод для удаления записи из ЧС
     *
     * @param externalUserId Id внешнего пользователя
     * @param targetUserId   Id целевого пользователя
     * @throws BadRequestException в случае, если пользователь попробует сам себя добавить в ЧС
     * @throws NotFoundException   в случае, если пользователя нет в ЧС
     * @throws ConflictException   в случае, если пользователь уже был удален из ЧС
     */
    @Transactional
    public void deleteNote(UUID externalUserId, UUID targetUserId) {
        commonService.checkUserExisting(externalUserId);

        if (targetUserId.equals(externalUserId)) {
            throw new BadRequestException("Пользователь не может добавить сам себя в черный список!");
        }

        var entity = blackListRepository.findByTargetUserIdAndAddingUserId(targetUserId, externalUserId)
                .orElseThrow(() -> new NotFoundException("Пользователя нет в черном списке!"));

        if (entity.getDeleteNoteDate() != null) {
            throw new ConflictException("Пользователь с таким id уже удален из черного списка!");
        } else {
            entity.setDeleteNoteDate(new Date());
            blackListRepository.save(entity);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            var dto = CreateNotificationDto.builder()
                    .userId(externalUserId)
                    .text("Вас удалил из ЧС в " + formattedDateTime + " пользователь с id: " + targetUserId)
                    .type(NotificationType.BLACKLIST_REMOVE)
                    .build();
            sendNotification(dto);
        }
    }

    /**
     * Метод для получение данных из БД с пагинацией
     *
     * @param sortingDto   ДТО с данными для пагинации
     * @param targetUserId Id целевого пользователя
     * @return {@link OutputNotesPageDto}
     * @throws BadRequestException в случае, если номер страницы превыет число онных
     */
    @Transactional(readOnly = true)
    public OutputNotesPageDto getNotes(SortingDto sortingDto, UUID targetUserId) {
        var pageInfo = sortingDto.getPageInfo();
        paginationDetailsService.checkPagination(pageInfo.getPageNumber(), pageInfo.getPageSize());

        var filtersInfo = sortingDto.getFilters();
        Example<BlockedUserEntity> example = setupBlocakedUserEntityExample(filtersInfo, targetUserId);

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        Page<BlockedUserEntity> blockedUsersPage = blackListRepository.findAll(example, pageable);

        var nameFilter = filtersInfo.getFullName() == null ? "" : filtersInfo.getFullName();
        List<BlockedUserEntity> fullNameList = blackListRepository.findByFullNameLikeAndTargetUserId(nameFilter, targetUserId);

        if (blockedUsersPage.getTotalPages() <= pageInfo.getPageNumber() - 1 && blockedUsersPage.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных");
        }

        return PageMapper.pageToOutputNotesPageDto(blockedUsersPage, fullNameList);
    }

    /**
     * Метод для получения {@link Example}<{@link BlockedUserEntity}>
     *
     * @param filtersInfo  объект {@link PageFiltersDto} с фильтрами
     * @param targetUserId Id целевого пользователя
     * @return {@link Example}<{@link BlockedUserEntity}>
     */
    private Example<BlockedUserEntity> setupBlocakedUserEntityExample(PageFiltersDto filtersInfo, UUID targetUserId) {
        var exampleFriend = BlockedUserEntity.from(
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
     * @return {@link OutputNotesPageDto}
     * @throws BadRequestException в случае, если номер страницы превыет число онных
     */
    @Transactional(readOnly = true)
    public OutputNotesPageDto searchNotes(SearchDto searchDto, UUID targetUserId) {
        var pageInfo = searchDto.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());

        Page<BlockedUserEntity> blocakedUsersPage = blackListRepository.findAllByTargetUserId(targetUserId, pageable);
        var nameFilter = searchDto.getFilterName() == null ? "" : searchDto.getFilterName();

        List<BlockedUserEntity> fullNameList = blackListRepository.findByFullNameLikeAndTargetUserId(nameFilter, targetUserId);

        if (blocakedUsersPage.getTotalPages() <= pageInfo.getPageNumber() - 1 && blocakedUsersPage.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных");
        }

        return PageMapper.pageToOutputNotesPageDto(blocakedUsersPage, fullNameList);
    }

    /**
     * Метод для обновления поля fullName для всех записей в БД
     *
     * @param externalUserId Id внешнего пользователя
     * @throws NotFoundException в случае, если пользователя нет в ЧС
     */
    @Transactional
    public void updateFullNameFields(UUID externalUserId) {
        commonService.checkUserExisting(externalUserId);
        var user = commonService.getUserById(externalUserId);

        blackListRepository.updateFullNameByAddingUserId(externalUserId, user.getFullName());
    }

    /**
     * Метод для проверки того, находится ли целевой пользователь в ЧС внешнего пользователя
     *
     * @param targetUserId   Id целевого пользователя
     * @param externalUserId Id внешнего пользователя
     * @return {@link Boolean}
     */
    @Transactional
    public Boolean checkIfTargetUserInExternalUsersBlackList(UUID targetUserId, UUID externalUserId) {
        return blackListRepository.existsByTargetUserIdAndAddingUserIdAndDeleteNoteDate(externalUserId, targetUserId, null);
    }

    /**
     * Метод для проверки того, находится ли внешний пользователь в ЧС целевого пользователя
     *
     * @param targetUserId   Id целевого пользователя
     * @param externalUserId Id внешнего пользователя
     * @return {@link Boolean}
     */
    @Transactional
    public Boolean checkIfUserInBlackList(UUID targetUserId, UUID externalUserId) {
        return blackListRepository.existsByTargetUserIdAndAddingUserIdAndDeleteNoteDate(targetUserId, externalUserId, null);
    }

    /**
     * Метод для отсылания уведомления в сервис уведомлений через {@link StreamBridge}
     *
     * @param dto ДТО для создания уведомления
     */
    private void sendNotification(CreateNotificationDto dto) {
        streamBridge.send("notificationEvent-out-0", dto);
    }
}
