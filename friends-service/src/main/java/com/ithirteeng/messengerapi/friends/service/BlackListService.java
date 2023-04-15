package com.ithirteeng.messengerapi.friends.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnablePaginationService
@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    private final CommonService commonService;

    private final FriendsService friendsService;

    private final CheckPaginationDetailsService paginationDetailsService;

    @Transactional(readOnly = true)
    public FullNoteDto getNoteData(UUID targetId, UUID externalUserId) {
        commonService.checkUserExisting(externalUserId);

        var entity = blackListRepository.findByTargetUserIdAndAddingUserId(targetId, externalUserId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + externalUserId + " не находится в черном списке"));

        if (entity.getDeleteNoteDate() != null) {
            throw new BadRequestException("Пользователь с id: " + externalUserId + " удален из черного списка");
        } else {
            return BlackListMapper.fullDtoFromEntity(entity);
        }
    }

    @Transactional
    public void addNote(UUID externalUserId, UUID targetUserId) {
        commonService.checkUserExisting(targetUserId);

        if (targetUserId == externalUserId) {
            throw new BadRequestException("Пользователь не может добавить сам себя в черный список!");
        }

        var externalUser = commonService.getUserById(externalUserId);

        addNoteToTargetUser(targetUserId, externalUser);
        try {
            friendsService.deleteFriend(externalUserId, targetUserId);
        } catch (Exception e) {
            if (!(e instanceof ConflictException || e instanceof NotFoundException)) {
                throw new RuntimeException(e);
            }
        }
    }

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

    @Transactional
    public void deleteNote(UUID externalUserId, UUID targetUserId) {
        commonService.checkUserExisting(externalUserId);

        if (targetUserId == externalUserId) {
            throw new BadRequestException("Пользователь не может сам себя удалить из черного списка!");
        }

        var entity = blackListRepository.findByTargetUserIdAndAddingUserId(targetUserId, externalUserId)
                .orElseThrow(() -> new NotFoundException("Пользователя нет в черном списке!"));

        if (entity.getDeleteNoteDate() != null) {
            throw new ConflictException("Пользователь с таким id уже удален из черного списка!");
        } else {
            entity.setDeleteNoteDate(new Date());
            blackListRepository.save(entity);
        }
    }

    @Transactional(readOnly = true)
    public OutputNotesPageDto getNotes(SortingDto sortingDto, UUID targetUserId) {
        var pageInfo = sortingDto.getPageInfo();
        paginationDetailsService.checkPagination(pageInfo.getPageNumber(), pageInfo.getPageSize());

        var filtersInfo = sortingDto.getFilters();
        Example<BlockedUserEntity> example = setupBlocakedUserEntityExample(filtersInfo, targetUserId);

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());
        Page<BlockedUserEntity> blockedUsersPage = blackListRepository.findAll(example, pageable);

        var nameFilter = filtersInfo.getFullName() == null ? "" : filtersInfo.getFullName();
        List<BlockedUserEntity> fullNameList = blackListRepository.findByFullNameLikeAndTargetUserId(nameFilter, targetUserId);

        if (blockedUsersPage.getTotalPages() <= pageInfo.getPageNumber() && blockedUsersPage.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных - 1");
        }

        return PageMapper.pageToOutputNotesPageDto(blockedUsersPage, fullNameList);
    }

    private Example<BlockedUserEntity> setupBlocakedUserEntityExample(PageFiltersDto filtersInfo, UUID targetUserId) {
        var exampleFriend = BlockedUserEntity.from(
                filtersInfo.getAddingDate(),
                filtersInfo.getDeletingDate(),
                filtersInfo.getExternalId(),
                targetUserId
        );
        return Example.of(exampleFriend);
    }

    @Transactional(readOnly = true)
    public OutputNotesPageDto searchNotes(SearchDto searchDto, UUID targetUserId) {
        var pageInfo = searchDto.getPageInfo();
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());

        Page<BlockedUserEntity> blocakedUsersPage = blackListRepository.findAllByTargetUserId(targetUserId, pageable);
        var nameFilter = searchDto.getFilterName() == null ? "" : searchDto.getFilterName();

        List<BlockedUserEntity> fullNameList = blackListRepository.findByFullNameLikeAndTargetUserId(nameFilter, targetUserId);

        if (blocakedUsersPage.getTotalPages() <= pageInfo.getPageNumber() && blocakedUsersPage.getTotalPages() != 0) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных - 1");
        }

        return PageMapper.pageToOutputNotesPageDto(blocakedUsersPage, fullNameList);
    }

    @Transactional
    public void updateFullNameFields(UUID friendId, UUID targetUserId) {
        commonService.checkUserExisting(friendId);
        var user = commonService.getUserById(friendId);

        if (!blackListRepository.existsByAddingUserIdAndTargetUserId(friendId, targetUserId)) {
            throw new NotFoundException("Пользователя нет в черном списке!");
        }

        blackListRepository.updateFullNameByAddingUserId(friendId, user.getFullName());
    }
}
