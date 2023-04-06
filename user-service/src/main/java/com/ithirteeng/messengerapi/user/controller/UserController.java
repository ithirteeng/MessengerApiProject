package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.user.dto.*;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.service.AuthenticationService;
import com.ithirteeng.messengerapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ithirteeng.messengerapi.common.security.consts.RequestsConstants.AUTHORIZATION_HEADER;

/**
 * RestController для user - модуля
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final SecurityProps securityProps;

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registerUser(@Validated @RequestBody RegistrationDto registrationDto) {
        var body = userService.postRegistration(registrationDto);
        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, authenticationService.generateJwt(registrationDto))
                .body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@Validated @RequestBody LoginDto loginDto) {
        var body = userService.postLogin(loginDto);
        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, authenticationService.generateJwt(loginDto))
                .body(body);
    }

    @GetMapping("/profile")
    public UserDto getProfileData(@Validated @RequestBody GetProfileDto dto) {
        return UserMapper.entityToUserDto(userService.getUserByLogin(dto.getLogin()));
    }

    @PutMapping("/profile/change")
    public UserDto changeProfileData(
            @Validated @RequestBody UpdateProfileDto updateProfileDto,
            Authentication authentication
    ) {
        var userData = (JwtUserDetails) authentication.getPrincipal();

        return userService.updateProfile(updateProfileDto, userData.getLogin());

    }
}