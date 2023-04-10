package com.ithirteeng.messengerapi.common.security.jwt;

import com.ithirteeng.messengerapi.common.exception.UnauthorizedException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.ithirteeng.messengerapi.common.security.consts.RequestsConstants.AUTHORIZATION_HEADER;
import static com.ithirteeng.messengerapi.common.security.consts.RequestsConstants.BEARER_PREFIX;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String jwtSecretKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, UnauthorizedException {
        var bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException("Отсутсвует хэдер Authorization!");
        } else if (!bearerToken.contains(BEARER_PREFIX)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new UnauthorizedException("Формат аутентификации должен быть Bearer token!");
        }

        var jwtToken = bearerToken.substring(7);

        var userData = parseToken(jwtToken);

        var authentication = new JwtAuthentication(userData);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private JwtUserDetails parseToken(String jwtToken) throws UnauthorizedException {
        JwtUserDetails userData;
        try {
            var secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
            var data = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken);

            var userIdString = String.valueOf(data.getBody().get("id"));
            userData = new JwtUserDetails(
                    userIdString == null ? null : UUID.fromString(userIdString),
                    String.valueOf(data.getBody().get("login")),
                    String.valueOf(data.getBody().get("fullName"))
            );
        } catch (JwtException e) {
            throw new UnauthorizedException(e.getMessage());
        }

        return userData;
    }
}
