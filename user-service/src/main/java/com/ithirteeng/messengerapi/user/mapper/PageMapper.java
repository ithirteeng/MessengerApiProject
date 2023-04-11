package com.ithirteeng.messengerapi.user.mapper;

import com.ithirteeng.messengerapi.user.dto.OutputPageDto;
import com.ithirteeng.messengerapi.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {
    public static OutputPageDto pageToOutputPageDto(Page<UserDto> page) {
        return OutputPageDto.builder()
                .users(page.getContent())
                .pageNumber(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .totalPages(page.getTotalPages())
                .build();
    }
}
