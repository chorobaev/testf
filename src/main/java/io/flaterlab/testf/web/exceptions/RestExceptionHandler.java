package io.flaterlab.testf.web.exceptions;

import io.flaterlab.testf.security.jwt.InvalidJwtAuthenticationException;
import io.flaterlab.testf.utils.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = {TestNotFoundException.class})
    public ResponseEntity testNotFound(TestNotFoundException exception, WebRequest request) {
        log.debug("handling TestNotFoundException...");
        return notFound().build();
    }

    @ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
    public ResponseEntity invalidJwtAuthentication(InvalidJwtAuthenticationException ex, WebRequest request) {
        System.out.println("handling InvalidJwtAuthenticationException...");
        return status(HttpStatus.UNAUTHORIZED).body(Json.builder().put("message", "You are not authorized").build());
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity badCredentials(BadCredentialsException ex, WebRequest request) {
        System.out.println("handling BadCredentialsException...");
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).build());
    }
}