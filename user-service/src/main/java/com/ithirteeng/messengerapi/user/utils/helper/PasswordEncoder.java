package com.ithirteeng.messengerapi.user.utils.helper;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 * Вспомогательный класс для кодирования пароля и сопоставления закодированного с незакодированным
 */
public class PasswordEncoder {

    private static final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(
            16, 32, 1, 65536, 10
    );

    /**
     * Метод для закодирования пароля
     *
     * @param password пароль
     * @return закодированный с помощью {@link Argon2PasswordEncoder} пароль
     */
    public static String encodePassword(String password) {
        return encoder.encode(password);
    }

    /**
     * Метод для проверки схождения паролей
     *
     * @param rawPassword "сырой" или введенный пользователем пароль
     * @param encodedPassword закодированный пароль
     * @return {@link Boolean}
     */
    public static Boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}
