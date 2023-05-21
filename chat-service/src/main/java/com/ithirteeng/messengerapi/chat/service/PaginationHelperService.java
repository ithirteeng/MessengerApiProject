package com.ithirteeng.messengerapi.chat.service;

import com.ithirteeng.messengerapi.chat.dto.common.PageInfoDto;
import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пагинацией
 */
@Service
@RequiredArgsConstructor
public class PaginationHelperService {
    /**
     * Метод для получения корректного списка в зависимости от параметров пагинации
     *
     * @param list общий список
     * @param infoDto ДТО для информации с пагинацией
     * @return корректны список в зависимости от параметров пагинации
     * @param <T> Тип данных списка
     */
    public static <T> List<T> getCorrectPageList(List<T> list, PageInfoDto infoDto) {
        Integer totalPages = getTotalPagesCount(list, infoDto);
        if (infoDto.getPageNumber() >= totalPages) {
            throw new BadRequestException("Данные пагинции не верны!");
        }

        if (list.size() < infoDto.getPageSize()) {
            return list;
        } else {
            var firstIndex = infoDto.getPageNumber() * infoDto.getPageSize();
            var secondIndex = (infoDto.getPageNumber() + 1) * infoDto.getPageSize();
            if (secondIndex >= list.size()) {
                secondIndex = list.size();
            }
            return list.subList(firstIndex, secondIndex);
        }
    }

    /**
     * Метод для получения общего количества страниц в зависимости от параметров пагинации
     *
     * @param list список
     * @param infoDto ДТО для информации с пагинацией
     * @return количество страниц
     * @param <T> Тип данных списка
     */
    public static <T> Integer getTotalPagesCount(List<T> list, PageInfoDto infoDto) {
        if (list.size() == 0) {
            return 1;
        }
        return (int) Math.ceil((float) list.size() / infoDto.getPageSize());
    }
}
