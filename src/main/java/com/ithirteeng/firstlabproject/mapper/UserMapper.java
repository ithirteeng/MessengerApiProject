package com.ithirteeng.firstlabproject.mapper;

import com.ithirteeng.firstlabproject.dto.CreateUpdateUserDto;
import com.ithirteeng.firstlabproject.dto.UserDto;
import com.ithirteeng.firstlabproject.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class UserMapper {
    public static UserEntity newUserToEntity(CreateUpdateUserDto createUpdateUserDto) {
        return new UserEntity(
                UUID.randomUUID().toString(),
                createUpdateUserDto.getLogin(),
                createUpdateUserDto.getPassword(),
                createUpdateUserDto.getName(),
                createUpdateUserDto.getSurname(),
                createUpdateUserDto.getPatronymic(),
                createUpdateUserDto.getBirthDate(),
                new Date()
        );
    }

    public static UserDto entityToDto(UserEntity userEntity) {
        return new UserDto(
                userEntity.getUuid(),
                userEntity.getLogin(),
                userEntity.getPassword(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getPatronymic(),
                userEntity.getBirthDate(),
                userEntity.getRegistrationDate()
        );
    }
}
