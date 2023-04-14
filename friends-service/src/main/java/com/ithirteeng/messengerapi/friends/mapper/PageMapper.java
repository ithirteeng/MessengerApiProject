package com.ithirteeng.messengerapi.friends.mapper;

import com.ithirteeng.messengerapi.friends.dto.friendlist.OutputFriendsPageDto;
import com.ithirteeng.messengerapi.friends.dto.friendlist.ShortFriendDto;
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
     * Метод для преобразования объектов класса {@link Page} в объект класса {@link OutputFriendsPageDto}
     *
     * @param page объект класса {@link Page}
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

    public static List<ShortFriendDto> mapEntityListToDtoList(List<FriendEntity> list) {
        var newList = new ArrayList<ShortFriendDto>();
        for (FriendEntity entity : list) {
            newList.add(FriendsMapper.shortDtoFromEntity(entity));
        }
        return newList;
    }

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