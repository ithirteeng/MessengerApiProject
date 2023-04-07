package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.user.dto.GetProfileDto;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.dto.UserDto;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * RestController для user - модуля
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public UserDto registerUser(@Validated @RequestBody RegistrationDto registrationDto) {
        return userService.postRegistration(registrationDto);
    }

    @PostMapping("/login")
    public UserDto loginUser(@Validated @RequestBody LoginDto loginDto) {
        return userService.postLogin(loginDto);
    }

    @GetMapping("/profile")
    public UserDto getProfileData(@Validated @RequestBody GetProfileDto dto) {
        return UserMapper.entityToUserDto(userService.getUserByLogin(dto.getLogin()));

    }
}
