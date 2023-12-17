package com.configserver.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BadRequestException extends ApplicationException {
    private static final HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;

    public BadRequestException(ApplicationError error) {
        super(httpStatusCode, error);
    }

    public BadRequestException(String errorCode, String message) {
        super(httpStatusCode, new ApplicationError(errorCode, message));
    }
}
