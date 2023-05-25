package com.ithirteeng.messengerapi.common.security.utils;

import com.ithirteeng.messengerapi.common.security.jwt.JwtUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SecurityUtil {
    public static JwtUserDetails getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtUserDetails) authentication.getPrincipal();
    }
}
