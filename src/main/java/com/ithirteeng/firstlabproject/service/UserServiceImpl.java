package com.ithirteeng.firstlabproject.service;

import com.ithirteeng.firstlabproject.dto.CreateUpdateUserDto;
import com.ithirteeng.firstlabproject.dto.UserDto;
import com.ithirteeng.firstlabproject.exception.BadRequestException;
import com.ithirteeng.firstlabproject.exception.ConflictException;
import com.ithirteeng.firstlabproject.mapper.UserMapper;
import com.ithirteeng.firstlabproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Transactional
    @Override
    public UserDto getUserByLogin(String login) throws BadRequestException {
        if (!repository.checkIdLoginExists(login)) {
            throw new BadRequestException("400: User doesn't exist");
        } else {
            var userEntity = repository.getUserByLogin(login);
            return UserMapper.entityToDto(userEntity);
        }

    }

    @Transactional
    @Override
    public String registerUser(CreateUpdateUserDto createUpdateUserDto) throws ConflictException {
        var userEntity = UserMapper.newUserToEntity(createUpdateUserDto);
        if (!repository.checkIdLoginExists(userEntity.getLogin())) {
            throw new ConflictException("409: User already exists!");
        } else {
            repository.save(userEntity);
            return "User created!";
        }
    }

    @Override
    public String updateUserData(CreateUpdateUserDto createUpdateUserDto, String id) throws BadRequestException {
        var userEntity = repository.findById(id)
                .orElseThrow(() -> new BadRequestException("400: User with this id doesn't exist"));
        userEntity.setPassword(createUpdateUserDto.getPassword());
        userEntity.setName(createUpdateUserDto.getName());
        userEntity.setSurname(createUpdateUserDto.getSurname());
        userEntity.setPatronymic(createUpdateUserDto.getPatronymic());
        userEntity.setBirthDate(createUpdateUserDto.getBirthDate());
        repository.save(userEntity);
        return "User updated!";
    }
}
