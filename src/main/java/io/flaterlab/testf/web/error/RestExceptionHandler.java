package io.flaterlab.testf.web.error;

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
public final class RestExceptionHandler {

    @ExceptionHandler(value = {TestNotFoundException.class})
    public ResponseEntity testNotFound(TestNotFoundException exception, WebRequest request) {
        return notFound().build();
    }

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity userAlreadyExists(UserAlreadyExistException ex, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).buildMap());
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity badCredentials(BadCredentialsException ex, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).buildMap());
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity badRequest(BadCredentialsException ex, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).buildMap());
    }
}