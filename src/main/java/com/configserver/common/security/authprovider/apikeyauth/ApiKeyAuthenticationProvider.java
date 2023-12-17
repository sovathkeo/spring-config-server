package com.configserver.common.security.authprovider.apikeyauth;

import com.configserver.common.helper.TypeSafeCastingHelper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {
        var xApiKeyAuth = TypeSafeCastingHelper.castToApiKeyAuthentication(authentication);

        if (xApiKeyAuth.isPresent() && isKeyValid(xApiKeyAuth.get())) {
            return ApiKeyAuthentication.successAuthenticate(xApiKeyAuth.get().getApiKey());
        }
        throw new AuthenticationServiceException("Invalid api key");
    }

    @Override
    public boolean supports( Class<?> authentication ) {
        return ApiKeyAuthentication.class.isAssignableFrom(authentication);
    }

    private boolean isKeyValid( ApiKeyAuthentication auth) {
        if (StringUtils.hasText(auth.getApiKey())) {
            return auth.getApiKey().equals("secret");
        }
        return false;
    }
}
