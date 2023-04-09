package com.ithirteeng.messengerapi.user.utils.helper;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Вспомогательный класс для кодирования пароля и сопоставления закодированного с незакодированным
 */
public class PasswordHelper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Метод для закодирования пароля
     *
     * @param password пароль
     * @return закодированный с помощью {@link PasswordHelper} пароль
     */
    public static String encodePassword(String password) {
        return encoder.encode(password);
    }

    /**
     * Метод для проверки схождения паролей
     *
     * @param rawPassword     "сырой" или введенный пользователем пароль
     * @param encodedPassword закодированный пароль
     * @return {@link Boolean}
     */
    public static Boolean isPasswordValid(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}
