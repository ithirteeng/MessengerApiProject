package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.common.enums.NotificationType;
import com.ithirteeng.messengerapi.common.model.CreateNotificationDto;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import com.ithirteeng.messengerapi.common.security.utils.SecurityUtil;
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
 * Контроллер для всех запросов, связанных с пользователями
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final StreamBridge streamBridge;

    /**
     * Метод для регистрации пользователя
     *
     * @param registrationDto ДТО ({@link RegistrationDto}) для регистрации пользователя
     * @return {@link ResponseEntity}<{@link UserDto}>
     */
    @PostMapping("/registration")
    public ResponseEntity<UserDto> registerUser(@Validated @RequestBody RegistrationDto registrationDto) {
        var body = userService.postRegistration(registrationDto);
        return ResponseEntity.ok()
                .header(AUTHORIZATION_HEADER, authenticationService.generateJwt(registrationDto))
                .body(body);
    }

    /**
     * Метод для входа пользователя
     *
     * @param loginDto ДТО ({@link LoginDto}) для логина пользователя
     * @return {@link ResponseEntity}<{@link UserDto}>
     */
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

    /**
     * Метод для отсылания уведомления в сервис уведомлений через {@link StreamBridge}
     *
     * @param dto ДТО для создания уведомления
     */
    private void sendNotification(CreateNotificationDto dto) {
        streamBridge.send("notificationEvent-out-0", dto);
    }

    /**
     * Метод для получения данных профиля пользователя по данным
     *
     * @param dto            ДТО ({@link GetProfileDto}) для получения данных профиля
     * @return {@link UserDto}
     */
    @GetMapping("/profile")
    public UserDto getProfileData(@Validated @RequestBody GetProfileDto dto) {
        var userData = SecurityUtil.getUserData();
        return userService.getUserData(dto.getLogin(), userData.getId());
    }

    /**
     * Метод для получения данных профиля пользователя
     *
     * @return {@link UserDto}
     */
    @GetMapping("/me")
    public UserDto getUserData() {
        var userData = SecurityUtil.getUserData();
        return UserMapper.entityToUserDto(userService.findUserEntityById(userData.getId()));
    }

    /**
     * Методя для изменения данных пользователя
     *
     * @param updateProfileDto ДТО ({@link UpdateProfileDto}) для изменения данных поьзователя
     * @return {@link UserDto}
     */
    @PutMapping("/profile/change")
    public UserDto changeProfileData(@Validated @RequestBody UpdateProfileDto updateProfileDto) {
        var userData = SecurityUtil.getUserData();
        return userService.updateProfile(updateProfileDto, userData.getLogin());

    }

    /**
     * Метод для получения данных о пользователях по страницам, опираясь на параметры пагинации
     *
     * @param sortingDto ДТО с параметрами для пагинации
     * @return {@link OutputPageDto}
     */
    @PostMapping("/list")
    public OutputPageDto getUsersList(@Validated @RequestBody SortingDto sortingDto) {
        return PageMapper.pageToOutputPageDto(userService.getUsersList(sortingDto));
    }
}
