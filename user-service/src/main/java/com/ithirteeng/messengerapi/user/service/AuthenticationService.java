package com.ithirteeng.messengerapi.user.service;

import com.ithirteeng.messengerapi.common.exception.BadRequestException;
import com.ithirteeng.messengerapi.common.exception.NotFoundException;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import com.ithirteeng.messengerapi.user.dto.LoginDto;
import com.ithirteeng.messengerapi.user.dto.RegistrationDto;
import com.ithirteeng.messengerapi.user.entity.UserEntity;
import com.ithirteeng.messengerapi.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final SecurityProps securityProps;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String generateJwt(LoginDto loginDto) {
        var userEntity = userRepository.findByLogin(loginDto.getLogin())
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));
        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            throw new BadRequestException("Неверный пароль!");
        }
        return generateJwtTokenForUser(userEntity);
    }

    public String generateJwt(RegistrationDto registrationDto) {
        var userEntity = userRepository.findByLogin(registrationDto.getLogin())
                .orElseThrow(() -> new NotFoundException("Такого пользователя не существует"));
        return generateJwtTokenForUser(userEntity);
    }


    private String generateJwtTokenForUser(UserEntity userEntity) {
        var key = Keys.hmacShaKeyFor(securityProps.getJwtToken().getSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(userEntity.getFullName())
                .setClaims(Map.of(
                        "login", userEntity.getLogin(),
                        "id", userEntity.getId(),
                        "fullName", userEntity.getFullName()
                ))
                .setId(UUID.randomUUID().toString())
                .setExpiration(new Date(currentTimeMillis() + securityProps.getJwtToken().getExpiration()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
