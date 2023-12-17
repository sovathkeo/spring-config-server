package com.configserver.common.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.configserver.common.alerts.TelegramBot;
import com.configserver.dtos.responses.ResponseImpl;
import com.configserver.services.tracing.CorrelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private CorrelationService correlationService;

    @ExceptionHandler(value = {RuntimeException.class, ApplicationException.class , ApplicationRuntimeException.class})
    public ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) throws JsonProcessingException {
        return handleApplicationException(ex, request);
    }

    @ExceptionHandler(value = {Exception.class,  AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<Object> handleCustomException1(RuntimeException ex, WebRequest request) throws JsonProcessingException{

        var applicationException = tryGetApplicationException(ex);
        if (applicationException.isPresent()) {
            return handleApplicationException(applicationException.get(), request);
        }

        var correlationId = correlationService.getCorrelationId();
        var body = ResponseImpl.Failed(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "failed",
                new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),ex.getMessage()),
                correlationId
            );

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request ) {
        var correlationId = correlationService.getCorrelationId();
        var body = ResponseImpl.Failed(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "failed",
            new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR.toString(),ex.getMessage()),
            correlationId
        );

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleApplicationException(
        Exception ex,
        WebRequest request) throws JsonProcessingException{

        var correlationId = correlationService.getCorrelationId();
        var e = tryGetApplicationException(ex);
        var statusCode = HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
        var appError = ApplicationError.applicationNone();
        if (e.isPresent()) {
            statusCode = e.get().getHttpStatusCode();
            appError = e.get().getError();

        }
        var body = ResponseImpl.Failed(statusCode.value(),"failed",appError,correlationId);
        var bodyString = mapper.writeValueAsString(body);
        logger.error(bodyString, e);

        telegramBot.sendMessageAsync(body);

        return handleExceptionInternal(ex, body, new HttpHeaders(), statusCode, request);
    }

    private Optional<ApplicationException> tryGetApplicationException(Exception exception) {
        var ex = exception.getCause();
        while (true) {
            if (ex == null) {
                return Optional.empty();
            }
            if (ex instanceof ApplicationException) {
                return Optional.of((ApplicationException) ex);
            }
            ex = ex.getCause();
        }
    }
}
