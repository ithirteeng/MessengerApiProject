package com.ithirteeng.messengerapi.user.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.ConflictException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.dto.UpdateProfileDto;
import com.ithirteeng.messengerapi.user.dto.UserDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import com.ithirteeng.messengerapi.user.utils.helper.PasswordHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Сервис для работы с данными о юзере
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * Метод для получения данных о пользователе по id
     *
     * @param id id пользователя в формате {@link UUID}
     * @return объект типа {@link UserEntity}
     * @throws NotFoundException возникает при несуществующем id пользователя
     */
    @Transactional(readOnly = true)
    public UserEntity getUserEntityById(UUID id) {
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
     * @throws ConflictException возникает при уже существующих email или login
     */
    @Transactional
    public UserDto updateProfile(UpdateProfileDto updateProfileDto, String login) {
        var entity = repository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));
        entity = UserMapper.updateUserFields(entity, updateProfileDto);
        repository.save(entity);
        return UserMapper.entityToUserDto(entity);

    }


}
