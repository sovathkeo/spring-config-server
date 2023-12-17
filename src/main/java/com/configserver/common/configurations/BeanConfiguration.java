package com.configserver.common.configurations;

import com.configserver.common.interceptor.RestTemplateAddHeaderInterceptor;
import com.configserver.services.tracing.CorrelationService;
import com.configserver.services.tracing.CorrelationServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class BeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CorrelationService correlationService() {
        return new CorrelationServiceImpl();
    }

    @Bean
    public RestTemplate restTemplate( RestTemplateBuilder builder, RestTemplateAddHeaderInterceptor restTemplateAddHeaderInterceptor) {

        var restTemplate = builder
            .build();

        restTemplate.setInterceptors(Collections.singletonList(restTemplateAddHeaderInterceptor));

        return restTemplate;
    }
}
