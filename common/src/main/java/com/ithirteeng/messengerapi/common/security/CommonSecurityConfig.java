package com.ithirteeng.messengerapi.common.security;

import com.ithirteeng.messengerapi.common.security.jwt.JwtTokenFilter;
import com.ithirteeng.messengerapi.common.security.props.SecurityProps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class CommonSecurityConfig {

    private final SecurityProps securityProps;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain setupJwtFilterChain(HttpSecurity http) {
        http = http
                .addFilterBefore(
                        new JwtTokenFilter(securityProps.getJwtToken().getSecret()),
                        UsernamePasswordAuthenticationFilter.class
                )
                .requestMatcher(
                        filterPredicate(
                                securityProps.getJwtToken().getRootPath(),
                                securityProps.getJwtToken().getPermitAll()
                        )
                );
        return finalize(http);
    }

    @SneakyThrows
    private SecurityFilterChain finalize(HttpSecurity http) {
        return http.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

    private RequestMatcher filterPredicate(String rootPath, String... ignore) {
        return rq -> Objects.nonNull(rq.getServletPath())
                && rq.getServletPath().startsWith(rootPath)
                && Arrays.stream(ignore).noneMatch(item -> rq.getServletPath().startsWith(item));
    }

}
