package com.configserver.common.exceptions;

import org.springframework.http.HttpStatusCode;

public class ApplicationException extends Exception{
    protected HttpStatusCode httpStatusCode;
    private final ApplicationError error;

    public ApplicationException(HttpStatusCode httpStatusCode, ApplicationError error) {
        this.httpStatusCode = httpStatusCode;
        this.error = error;
    }

    public ApplicationError getError() {
        return this.error;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}
