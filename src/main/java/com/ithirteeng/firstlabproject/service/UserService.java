package com.ithirteeng.firstlabproject.service;

import com.ithirteeng.firstlabproject.dto.CreateUpdateUserDto;
import com.ithirteeng.firstlabproject.dto.UserDto;
import com.ithirteeng.firstlabproject.exception.BadRequestException;
import com.ithirteeng.firstlabproject.exception.ConflictException;

public interface UserService {

    UserDto getUserByLogin(String login) throws BadRequestException;

    String registerUser(CreateUpdateUserDto createUpdateUserDto) throws ConflictException;

    String updateUserData(CreateUpdateUserDto createUpdateUserDto, String id) throws BadRequestException;

}
