package com.configserver.common.exceptions;

import org.springframework.http.HttpStatusCode;

public class ApplicationRuntimeException  extends RuntimeException implements IApplicationException{
    public ApplicationRuntimeException(Throwable e) {
        super(e);
    }

    @Override
    public HttpStatusCode getHttpStatusCode() {
        return null;
    }

    @Override
    public ApplicationError getError() {
        return null;
    }
}
