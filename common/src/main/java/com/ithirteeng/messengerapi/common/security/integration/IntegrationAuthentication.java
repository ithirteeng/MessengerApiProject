package com.ithirteeng.messengerapi.common.security.integration;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Данные {@link org.springframework.security.core.Authentication} по интеграционному взаимодейтсвию.
 */
public class IntegrationAuthentication extends AbstractAuthenticationToken {

    public IntegrationAuthentication() {
        super(null);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
