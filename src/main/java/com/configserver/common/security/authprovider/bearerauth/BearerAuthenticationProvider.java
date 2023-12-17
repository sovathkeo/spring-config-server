package com.configserver.common.security.authprovider.bearerauth;

import com.configserver.common.helper.TypeSafeCastingHelper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class BearerAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {

        var bearerAuth = TypeSafeCastingHelper.castToBearerAuthentication(authentication);

        if (bearerAuth.isEmpty()) {
            throw new AuthenticationServiceException("bearer auth casting error");
        }

        var principle = authentication.getPrincipal();

        if (principle == null || !principle.equals("ADMIN")) {
            throw new AuthenticationServiceException("bearer, credential invalid");
        }

        return new BearerAuthentication(principle.toString(), true);
    }

    @Override
    public boolean supports( Class<?> authentication ) {
        return BearerAuthentication.class.isAssignableFrom(authentication);
    }
}
