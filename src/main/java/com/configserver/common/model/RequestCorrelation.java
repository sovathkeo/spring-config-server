package com.configserver.common.model;


public class RequestCorrelation {

    private String correlationId;

    public RequestCorrelation( String correlationId ) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId( String correlationId ) {
        this.correlationId = correlationId;
    }
}
