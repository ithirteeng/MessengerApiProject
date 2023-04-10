package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.common.exception.UnauthorizedException;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final SecurityProps securityProps;

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
        try {
            return UserMapper.entityToUserDto(userService.getUserByLogin(dto.getLogin()));
        } catch (Error e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    @GetMapping("/test")
    public String test() {
        try {
            return securityProps.getIntegrations().getRootPath();
        } catch (Exception e) {
            return e.getMessage();
        }

    }
}
