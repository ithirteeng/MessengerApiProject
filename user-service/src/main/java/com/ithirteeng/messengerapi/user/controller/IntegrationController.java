package com.ithirteeng.messengerapi.user.controller;

import com.ithirteeng.messengerapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public Boolean checkUserExisingById(@PathVariable("id") UUID userId) {
        return userRepository.existsById(userId);
    }
}
