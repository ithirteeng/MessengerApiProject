package com.ithirteeng.messengerapi.common.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckPaginationDetailsService {
    /**
     * Метод для проверки на корректность номера страницы
     *
     * @param pageNumber номер страницы
     * @throws BadRequestException некорректный запрос
     */
    public void checkPageNumber(int pageNumber) {
        if (pageNumber < 0) {
            throw new BadRequestException("Номер страницы должен быть больше 0");
        }
    }

    /**
     * Метод для проверки на корректность размера страницы
     *
     * @param pageSize размер страницы
     * @throws BadRequestException некорректный запрос
     */
    public void checkPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new BadRequestException("Размер страницы должен быть больше 0");
        }
    }

    /**
     * Метод для проверки на null направления сортировки
     *
     * @param direction направление сортировки
     * @throws BadRequestException некорректный запрос
     */
    public void checkDirection(Sort.Direction direction) {
        if (direction == null) {
            throw new BadRequestException("Направление сортировки не должно быть null");
        }
    }

    /**
     * Общий метод для проверки всех данных необходимых для пагинации
     *
     * @param pageNumber номер страницы
     * @param pageSize   размер страницы
     */
    public void checkPagination(int pageNumber, int pageSize) {
        checkPageNumber(pageNumber);
        checkPageSize(pageSize);
    }
}
