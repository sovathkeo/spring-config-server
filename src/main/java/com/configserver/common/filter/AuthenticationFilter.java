package com.configserver.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.configserver.common.configurations.ApplicationConfiguration;
import com.configserver.common.constant.HttpHeaderConstant;
import com.configserver.common.exceptions.ApplicationError;
import com.configserver.common.security.authprovider.apikeyauth.ApiKeyAuthentication;
import com.configserver.common.security.authprovider.basicauth.BasicAuthentication;
import com.configserver.common.security.authprovider.bearerauth.BearerAuthentication;
import com.configserver.dtos.responses.ResponseImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    ApplicationConfiguration appConfig;

    final String AUTHORIZATION_HEADER = "Authorization";
    final String BEARER_PREFIX = "Bearer";
    final String BASIC_PREFIX = "Basic";

    public AuthenticationFilter( AuthenticationManager authenticationManager) {
        super("/**",authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException {

        if (isBypassAuth(request)) {
            return ApiKeyAuthentication.successAuthenticate("DISABLE_AUTH");
        }

        var token = request.getHeader(AUTHORIZATION_HEADER);

        var xApiKey = request.getHeader("X-API-KEY");

        if (xApiKey != null) {
            return getAuthenticationManager()
                .authenticate(new ApiKeyAuthentication(xApiKey));
        }

        if (!isOauthScheme(token)) {
            throw new AuthenticationServiceException("Authentication provider not supported");
        }

        return getAuthenticationManager()
            .authenticate(getAuthentication(token));
    }

    private boolean isOauthScheme(String token) {

        return token != null &&
            !token.isBlank() &&
            (token.startsWith(BEARER_PREFIX) || token.startsWith(BASIC_PREFIX));
    }

    private Authentication getAuthentication(String token) {
        if (isBearerScheme(token)) {
            return new BearerAuthentication(token);
        } else  {
            return new BasicAuthentication(token);
        }
    }

    private boolean isBearerScheme(String token) {
        return token.startsWith(BEARER_PREFIX);
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult ) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed ) throws IOException {

        var correlationId = request.getHeader(HttpHeaderConstant.CORRELATION_ID);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var res = ResponseImpl.Failed(
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            new ApplicationError(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), failed.getMessage()), correlationId);

        var body = mapper.writeValueAsString(res);

        PrintWriter writer = response.getWriter();

        writer.print(body);
        writer.flush();
        writer.close();
    }

    private boolean isBypassAuth(HttpServletRequest request) {
        return isInAuthWhitelist(request) || isMonitoringRequest(request);
    }

    private boolean isInAuthWhitelist(HttpServletRequest request) {
        var requestUri = request.getRequestURI();
        return Arrays.stream(appConfig.getEndpointsAuthWhitelist())
            .anyMatch(requestUri::startsWith);
    }

    private boolean isMonitoringRequest( HttpServletRequest request) {
        return request.getRequestURI().startsWith("/actuator");
    }
}
