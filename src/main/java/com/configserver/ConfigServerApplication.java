package com.configserver;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.configserver.common.constant.HttpHeaderConstant;
import com.configserver.common.constant.SystemEnvironmentConstant;
import com.configserver.common.constant.SystemPropertyNameConstant;
import com.configserver.common.helper.StringHelper;
import com.configserver.common.registration.RegisterService;
import jakarta.annotation.PostConstruct;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.UUID;

@SpringBootApplication
@EnableConfigServer
@EnableAsync(proxyTargetClass = true)
public class ConfigServerApplication {

    private static final String DEFAULT_CONFIG_FILE = "application";

    public static void main(String[] args) {
        MDC.put(HttpHeaderConstant.CORRELATION_ID, new UUID(0L, 0L).toString());
        System.setProperty(SystemPropertyNameConstant.SPRING_CONFIG_NAME, buildConfigFile());
        SpringApplication.run(ConfigServerApplication.class, args);
    }

    private static String buildConfigFile() {
        var profile = System.getenv(SystemEnvironmentConstant.SPRING_PROFILES_ACTIVE);
        return StringHelper.isNullOrEmpty(profile)
                ? DEFAULT_CONFIG_FILE
                : "%s-%s".formatted(DEFAULT_CONFIG_FILE, profile);
    }

    @PostConstruct
    public void init() {
        RegisterService.registers();
    }

    @Bean
    Pipeline pipeline(
            ObjectProvider<Command.Handler> commandHandler,
            ObjectProvider<Notification.Handler> notificationHandlers,
            ObjectProvider<Command.Middleware> middlewares) {

        return new Pipelinr()
                .with(commandHandler::stream)
                .with(notificationHandlers::stream)
                .with(middlewares::stream);
    }
}
