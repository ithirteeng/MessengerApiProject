package com.ithirteeng.messengerapi.user.mapper;

import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.dto.UserDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.utils.helper.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Маппер для преобразования объектов класса UserEntity в объекты UserDto,
 * а также преобразования объектов класса RegistrationDto в объекты UserEntity
 */
@Component
public class UserMapper {

    /**
     * Метод для преобразования объектов класса {@link RegistrationDto} в объекты {@link UserEntity}
     *
     * @param registrationDto DTO с данным о регистрации
     * @return объект типа {@link UserEntity}
     */
    public static UserEntity registrationDtoToUserEntity(RegistrationDto registrationDto) {
        return UserEntity.builder()
                .login(registrationDto.getLogin())
                .email(registrationDto.getEmail())
                .city(registrationDto.getCity())
                .password(PasswordEncoder.encodePassword(registrationDto.getPassword()))
                .registrationDate(new Date())
                .telephoneNumber(registrationDto.getTelephoneNumber())
                .avatarId(registrationDto.getAvatarId())
                .fullName(registrationDto.getFullName())
                .birthDate(registrationDto.getBirthDate())
                .build();
    }

    /**
     * Метод для преобразования объектов класса {@link UserEntity} в объекты {@link UserDto}
     *
     * @param userEntity класс с данными из БД
     * @return объект типа {@link UserDto}
     */
    public static UserDto entityToUserDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .login(userEntity.getLogin())
                .email(userEntity.getEmail())
                .fullName(userEntity.getFullName())
                .avatarId(userEntity.getAvatarId())
                .birthDate(userEntity.getBirthDate())
                .telephoneNumber(userEntity.getTelephoneNumber())
                .registrationDate(userEntity.getRegistrationDate())
                .city(userEntity.getCity())
                .build();
    }
}
