package com.ithirteeng.messengerapi.common.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtUserDetails {

    private UUID id;

    private String login;

    private String fullName;
}
