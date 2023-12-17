package com.configserver.common.security.authprovider.basicauth;

import com.configserver.common.enums.auth.AuthenticationSchemeEnum;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class BasicAuthentication implements Authentication {

    private String token;

    private String username;

    private String password;

    private boolean isAuthenticated;

    private List<SimpleGrantedAuthority> authorities;

    public BasicAuthentication( String token ) {
        this.token = token;
    }

    public BasicAuthentication( boolean isAuthenticated ) {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return AuthenticationSchemeEnum.BasicAuth;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated( boolean isAuthenticated ) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return this.username;
    }

    public String getToken() {
        return token;
    }
}
