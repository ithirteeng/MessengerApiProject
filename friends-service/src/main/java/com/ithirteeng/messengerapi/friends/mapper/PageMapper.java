package com.ithirteeng.messengerapi.friends.mapper;

import com.ithirteeng.messengerapi.friends.dto.blacklist.OutputNotesPageDto;
import com.ithirteeng.messengerapi.friends.dto.blacklist.ShortNoteDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.OutputFriendsPageDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.ShortFriendDto;
import com.ithirteeng.messengerapi.friends.entity.BlockedUserEntity;
import com.ithirteeng.messengerapi.friends.entity.FriendEntity;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Маппер для запросов с пагинацией
 */
@Component
public class PageMapper {
    /**
     * Метод для преобразования объектов {@link Page}<{@link FriendEntity}> и {@link List}<{@link FriendEntity}> в {@link OutputFriendsPageDto}
     *
     * @param page         Объект пагинации с {@link FriendEntity}
     * @param fullNameList Список пользователей {@link FriendEntity}, с подходящих по wildcard фильтру fullName
     * @return {@link OutputFriendsPageDto}
     */
    public static OutputFriendsPageDto pageToOutputPageDto(Page<FriendEntity> page, List<FriendEntity> fullNameList) {
        return OutputFriendsPageDto.builder()
                .friends(mapEntityListToDtoList(intersection(page.getContent(), fullNameList)))
                .pageNumber(page.getPageable().getPageNumber())
                .sortInfo(page.getPageable().getSort())
                .pageSize(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    /**
     * Метод для преобразования объектов {@link Page}<{@link BlockedUserEntity}> и {@link List}<{@link BlockedUserEntity}> в {@link OutputNotesPageDto}
     *
     * @param page         Объект пагинации с {@link BlockedUserEntity}
     * @param fullNameList Список пользователей {@link BlockedUserEntity}, с подходящих по wildcard фильтру fullName
     * @return {@link OutputNotesPageDto}
     */
    public static OutputNotesPageDto pageToOutputNotesPageDto(Page<BlockedUserEntity> page, List<BlockedUserEntity> fullNameList) {
        return OutputNotesPageDto.builder()
                .notes(mapEntityListToNotesDtoList(intersection(page.getContent(), fullNameList)))
                .pageNumber(page.getPageable().getPageNumber())
                .sortInfo(page.getPageable().getSort())
                .pageSize(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }

    /**
     *  Метод для преобразования объектов {@link List}<{@link FriendEntity}> в {@link List}<{@link ShortFriendDto}>
     *
     * @param list список сущностей {@link FriendEntity} из БД
     * @return {@link List}<{@link ShortFriendDto}>
     */
    public static List<ShortFriendDto> mapEntityListToDtoList(List<FriendEntity> list) {
        var newList = new ArrayList<ShortFriendDto>();
        for (FriendEntity entity : list) {
            newList.add(FriendsMapper.shortDtoFromEntity(entity));
        }
        return newList;
    }

    /**
     *  Метод для преобразования объектов {@link List}<{@link BlockedUserEntity}> в {@link List}<{@link ShortNoteDto}>
     *
     * @param list список сущностей {@link BlockedUserEntity} из БД
     * @return {@link List}<{@link ShortNoteDto}>
     */
    public static List<ShortNoteDto> mapEntityListToNotesDtoList(List<BlockedUserEntity> list) {
        var newList = new ArrayList<ShortNoteDto>();
        for (BlockedUserEntity entity : list) {
            newList.add(BlackListMapper.shortDtoFromEntity(entity));
        }
        return newList;
    }

    /**
     * Метод для пересечения двух списков
     *
     * @param list1 первый список
     * @param list2 второй список
     * @return {@link List}
     * @param <T> любой тип данных
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<>();

        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}