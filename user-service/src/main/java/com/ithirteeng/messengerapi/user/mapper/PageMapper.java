package com.ithirteeng.messengerapi.user.mapper;

import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.user.dto.OutputPageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Маппер для запросов с пагинацией
 */
@Component
public class PageMapper {
    /**
     * Метод для преобразования объектов класса {@link Page}<{@link UserDto}> в объект класса {@link OutputPageDto}
     *
     * @param page объект класса {@link Page}<{@link UserDto}>
     * @return {@link OutputPageDto}
     */
    public static OutputPageDto pageToOutputPageDto(Page<UserDto> page) {
        return OutputPageDto.builder()
                .users(page.getContent())
                .pageNumber(page.getPageable().getPageNumber())
                .sortInfo(page.getPageable().getSort())
                .pageSize(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }
}
