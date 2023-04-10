package com.ithirteeng.messengerapi.common.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class JwtUserDetails {

    private final UUID id;

    private final String login;

    private final String fullName;
}
