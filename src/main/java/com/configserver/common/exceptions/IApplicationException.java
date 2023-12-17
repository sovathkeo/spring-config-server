package com.configserver.common.exceptions;

import org.springframework.http.HttpStatusCode;

public interface IApplicationException {

    HttpStatusCode getHttpStatusCode();

    ApplicationError getError();

}
