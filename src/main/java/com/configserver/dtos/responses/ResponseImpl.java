package com.configserver.dtos.responses;

import com.configserver.common.exceptions.ApplicationError;
import com.configserver.dtos.base.AResponseBase;

public class ResponseImpl<TData extends AResponseBase> {

    private final int statusCode;
    private final String message;
    private final ApplicationError error;

    private final String correlationId;
    private final TData data;

    protected ResponseImpl(int statusCode, String message, TData data, ApplicationError error, String correlationId) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.error = error;
        this.correlationId = correlationId;
    }

    public static ResponseImpl<AResponseBase> Success(AResponseBase data, String correlationId) {
        return new ResponseImpl<>(200,"success",data, null, correlationId);
    }

    public static ResponseImpl<AResponseBase> Failed(
        int statusCode,
        String message,
        ApplicationError error,
        String correlationId) {

        return new ResponseImpl<>(statusCode,message,null, error, correlationId);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public TData getData() {
        return data;
    }

    public ApplicationError getError() {
        return error;
    }

    public String getCorrelationId() { return correlationId; }
}
