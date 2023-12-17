package com.configserver.common.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApplicationError {
    public String errorCode;
    public String message;

    public ApplicationError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @JsonIgnore
    public static ApplicationError applicationNone() {

        return new ApplicationError("-1","-1");
    }

    @JsonIgnore
    public boolean isNone() {
        return errorCode.equalsIgnoreCase("-1") && message.equalsIgnoreCase("-1");
    }
}