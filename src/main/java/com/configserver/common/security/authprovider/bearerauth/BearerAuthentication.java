package com.configserver.common.security.authprovider.bearerauth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class BearerAuthentication implements Authentication {

    private String token;
    private String principle;
    private boolean isAuthenticated;
    private List<SimpleGrantedAuthority> authorities;


    public BearerAuthentication(String token){
        this.token = token;
    }

    public BearerAuthentication( String principle, boolean isAuthenticated ) {
        this.principle = principle;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return "ADMIN";
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated( boolean isAuthenticated ) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return this.token;
    }
}
