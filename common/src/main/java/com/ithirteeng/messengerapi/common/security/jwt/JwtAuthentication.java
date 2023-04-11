package com.ithirteeng.messengerapi.common.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthentication extends AbstractAuthenticationToken {

    public JwtAuthentication(JwtUserDetails jwtUserDetails) {
        super(null);
        this.setDetails(jwtUserDetails);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
