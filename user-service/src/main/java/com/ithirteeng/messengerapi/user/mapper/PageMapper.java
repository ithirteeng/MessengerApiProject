package com.ithirteeng.messengerapi.user.mapper;

import com.ithirteeng.messengerapi.user.dto.OutputPageDto;
import com.ithirteeng.messengerapi.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Маппер для запросов с пагинацией
 */
@Component
public class PageMapper {
    /**
     * Метод для преобразования объектов класса {@link Page} в объект класса {@link OutputPageDto}
     *
     * @param page объект класса {@link Page}
     * @return {@link OutputPageDto}
     */
    public static OutputPageDto pageToOutputPageDto(Page<UserDto> page) {
        return OutputPageDto.builder()
                .users(page.getContent())
                .pageNumber(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }
}
