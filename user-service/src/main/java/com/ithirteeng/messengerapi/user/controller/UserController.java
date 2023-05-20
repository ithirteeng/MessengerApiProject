package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.user.dto.*;
import com.ithirteeng.messengerapi.user.mapper.PageMapper;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.service.AuthenticationService;
import com.ithirteeng.messengerapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ithirteeng.messengerapi.common.consts.RequestsConstants.AUTHORIZATION_HEADER;

/**
 * RestController для user - модуля
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final StreamBridge streamBridge;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        var dto = CreateNotificationDto.builder()
                .userId(body.getId())
                .text("Произошел вход в систему в " + formattedDateTime)
                .type(NotificationType.LOGIN)
                .build();
        sendNotification(dto);
        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, authenticationService.generateJwt(loginDto))
                .body(body);
    }

    private void sendNotification(CreateNotificationDto dto) {
        streamBridge.send("notificationEvent-out-0", dto);
    }

    @GetMapping("/profile")
    public UserDto getProfileData(@Validated @RequestBody GetProfileDto dto, Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return userService.getUserData(dto.getLogin(), userData.getId());
    }

    @GetMapping("/me")
    public UserDto getUserData(Authentication authentication) {
        var userData = (JwtUserDetails) authentication.getPrincipal();
        return UserMapper.entityToUserDto(userService.findUserEntityById(userData.getId()));
    }

    @PutMapping("/profile/change")
    public UserDto changeProfileData(
            @Validated @RequestBody UpdateProfileDto updateProfileDto,
            Authentication authentication
    ) {
        var userData = (JwtUserDetails) authentication.getPrincipal();

        return userService.updateProfile(updateProfileDto, userData.getLogin());

    }

    @PostMapping("/list")
    public OutputPageDto getUsersList(@Validated @RequestBody SortingDto sortingDto) {
        return PageMapper.pageToOutputPageDto(userService.getUsersList(sortingDto));
    }
}
