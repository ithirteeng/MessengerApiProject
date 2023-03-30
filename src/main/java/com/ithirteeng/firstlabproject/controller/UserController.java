package com.ithirteeng.firstlabproject.controller;

import com.ithirteeng.firstlabproject.dto.CreateUpdateUserDto;
import com.ithirteeng.firstlabproject.dto.LoginUserDto;
import com.ithirteeng.firstlabproject.service.UserService;
import com.ithirteeng.firstlabproject.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<String> registerUser(@Valid @RequestBody CreateUpdateUserDto dto) {
        try {
            return ResponseEntity.ok(userService.registerUser(dto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editUserData(@Valid @RequestBody CreateUpdateUserDto dto, @PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.updateUserData(dto, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> getUserDataByLogin(@Valid @RequestBody LoginUserDto dto) {
        try {
            return ResponseEntity.ok(userService.getUserByLogin(dto.getLogin()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
