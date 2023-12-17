package com.configserver.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResourceNotFoundException extends ApplicationException{

    private static final HttpStatusCode httpStatusCode = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String errorCode, String message) {
        super(httpStatusCode, new ApplicationError(errorCode,message));
    }
}
