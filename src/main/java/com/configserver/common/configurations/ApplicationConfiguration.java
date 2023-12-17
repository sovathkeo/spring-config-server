package com.configserver.common.configurations;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigServer
@ConfigurationProperties(prefix = "application-config")
public class ApplicationConfiguration {

    private String[] endpointsAuthWhitelist;


    public String[] getEndpointsAuthWhitelist() {
        return endpointsAuthWhitelist;
    }

    public void setEndpointsAuthWhitelist( String[] endpointsAuthWhitelist ) {
        this.endpointsAuthWhitelist = endpointsAuthWhitelist;
    }
}
