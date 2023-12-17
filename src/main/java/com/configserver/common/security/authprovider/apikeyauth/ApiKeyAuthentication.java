package com.configserver.common.security.authprovider.apikeyauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {

    public String getApiKey() {
        return apiKey;
    }

    private final String apiKey;

    public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    public ApiKeyAuthentication(String apiKey) {
        super(null);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.apiKey;
    }


    public static ApiKeyAuthentication successAuthenticate(String apiKey) {
        return new ApiKeyAuthentication(apiKey);
    }


}
