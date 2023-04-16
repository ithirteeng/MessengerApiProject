package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.model.UserDto;
import com.ithirteeng.messengerapi.user.mapper.UserMapper;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Контроллер для интеграционных запросов
 */
@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final UserRepository userRepository;

    @GetMapping("/users/check/{id}")
    public Boolean checkUserExisingById(@PathVariable("id") UUID userId) {
        return userRepository.existsById(userId);
    }

    @GetMapping("/users/data/{id}")
    public UserDto getUserById(@PathVariable("id") UUID userId) {
        var entity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с id: " + userId + " не существует"));
        return UserMapper.entityToUserDto(entity);
    }
}
