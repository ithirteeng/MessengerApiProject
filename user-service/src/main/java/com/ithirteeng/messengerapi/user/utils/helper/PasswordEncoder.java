package com.ithirteeng.messengerapi.user.utils.helper;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class PasswordEncoder {

    private static final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(
            16, 32, 1, 65536, 10
    );

    public static String encodePassword(String password) {
        return encoder.encode(password);
    }

    public static Boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}
