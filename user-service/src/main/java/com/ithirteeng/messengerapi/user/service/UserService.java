package com.ithirteeng.messengerapi.user.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.service.CheckPaginationDetailsService;
import com.ithirteeng.messengerapi.common.service.EnablePaginationService;
import com.ithirteeng.messengerapi.user.dto.*;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import com.ithirteeng.messengerapi.user.utils.helper.PasswordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Сервис для работы с данными о юзере
 */
@Service
@EnablePaginationService
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final CheckPaginationDetailsService paginationDetailsService;

    /**
     * Метод для получения данных о пользователе по id
     *
     * @param id id пользователя в формате {@link UUID}
     * @return объект типа {@link UserEntity}
     * @throws NotFoundException возникает при несуществующем id пользователя
     */
    @Transactional(readOnly = true)
    public UserEntity findUserEntityById(UUID id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователся с таким id " + id + " не существует"));
    }

    /**
     * Метод для получения данных о пользователе по логину
     *
     * @param login логин
     * @return объект типа {@link UserEntity}
     * @throws NotFoundException возникает при несуществующем логине
     */
    @Transactional(readOnly = true)
    public UserEntity getUserByLogin(String login) {
        return repository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким логином не существует!"));
    }

    /**
     * Метод для аутентификации по логину
     *
     * @param loginDto DTO с данными для логина
     * @return объект типа {@link UserDto}
     * @throws BadRequestException возникает при неверном пароле
     * @throws NotFoundException   возникает при несущесвующем логине
     */
    @Transactional(readOnly = true)
    public UserDto postLogin(LoginDto loginDto) {
        var entity = getUserByLogin(loginDto.getLogin());
        if (!PasswordHelper.isPasswordValid(loginDto.getPassword(), entity.getPassword())) {
            throw new BadRequestException("Неверный пароль");
        } else {
            return UserMapper.entityToUserDto(entity);
        }
    }

    /**
     * Метод для регистрации пользователя
     *
     * @param registrationDto DTO с данными для регистрации
     * @return объект типа {@link UserDto}
     * @throws ConflictException возникает при уже существующих email или login
     */
    @Transactional
    public UserDto postRegistration(RegistrationDto registrationDto) {
        if (repository.existsByLoginOrEmail(registrationDto.getLogin(), registrationDto.getEmail())) {
            throw new ConflictException("Такой пользователь уже существует!");
        } else {
            var entity = repository.save(UserMapper.registrationDtoToUserEntity(registrationDto));
            return UserMapper.entityToUserDto(entity);
        }

    }

    /**
     * Метод для изменения профиля пользователя
     *
     * @param updateProfileDto DTO с данными для Изменения профиля
     * @return объект типа {@link UserDto}
     * @throws NotFoundException возникает при несуществующем пользователе
     */
    @Transactional
    public UserDto updateProfile(UpdateProfileDto updateProfileDto, String login) {
        var entity = repository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));
        entity = UserMapper.updateUserFields(entity, updateProfileDto);
        repository.save(entity);
        return UserMapper.entityToUserDto(entity);
    }

    /**
     * Метод для получения данных по объекту класса {@link SortingDto} для пагинации
     *
     * @param sortingDto объект класса {@link SortingDto}
     * @return {@link Page<UserDto>}
     * @throws BadRequestException при номере страницы не должен превышать общее число онных - 1
     */
    @Transactional
    public Page<UserDto> getUsersList(SortingDto sortingDto) {
        var pageInfo = sortingDto.getPageInfo();
        paginationDetailsService.checkPagination(pageInfo.getPageNumber(), pageInfo.getPageSize());
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber(), pageInfo.getPageSize());

        var filtersInfo = sortingDto.getFilters();
        UserEntity exampleUser = UserEntity.from(
                filtersInfo.getFullName(),
                filtersInfo.getLogin(),
                filtersInfo.getEmail(),
                filtersInfo.getCity(),
                filtersInfo.getBirthDate(),
                filtersInfo.getTelephoneNumber()
        );

        Example<UserEntity> example = Example.of(exampleUser);

        Page<UserEntity> users = repository.findAll(example, pageable);

        if (users.getTotalPages() <= pageInfo.getPageNumber()) {
            throw new BadRequestException("Номер страницы не должен превышать общее число онных - 1");
        }
        return users.map(UserMapper::entityToUserDto);
    }


}
