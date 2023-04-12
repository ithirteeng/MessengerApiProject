package com.ithirteeng.messengerapi.common.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Данные {@link org.springframework.security.core.Authentication} по запросам с UI.
 * В качестве principal и details - {@link JwtUserDetails}
 */
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
