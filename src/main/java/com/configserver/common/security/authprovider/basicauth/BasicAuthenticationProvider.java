package com.configserver.common.security.authprovider.basicauth;

import com.configserver.common.enums.auth.AuthenticationSchemeEnum;
import com.configserver.common.helper.TypeSafeCastingHelper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Utf8;

import java.util.Base64;
import java.util.Objects;

public class BasicAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException {

        var basicAuthentication = TypeSafeCastingHelper.castToBasicAuthentication(authentication);

        if (basicAuthentication.isEmpty()) {
            throw new AuthenticationServiceException("basic auth casting error");
        }

        if (!isBasicAuth(basicAuthentication.get())) {
            throw new AuthenticationServiceException("basic auth, credential invalid");
        }

        var token = basicAuthentication
            .get()
            .getToken()
            .replace("Basic ", "");

        var credentials = Utf8.decode(Base64.getDecoder().decode(token));
        var username = credentials.split(":")[0];

        if (! Objects.equals(username, "ADMIN")) {
            throw new AuthenticationServiceException("basic, user not admin");
        }


        return new BasicAuthentication(true);
    }

    @Override
    public boolean supports( Class<?> authentication ) {
        return BasicAuthentication.class.isAssignableFrom(authentication);
    }

    private boolean isBasicAuth(BasicAuthentication basicAuthentication) {
        return basicAuthentication.getPrincipal() == AuthenticationSchemeEnum.BasicAuth;
    }
}
