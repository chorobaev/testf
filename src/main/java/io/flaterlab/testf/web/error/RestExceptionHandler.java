package io.flaterlab.testf.web.error;

import io.flaterlab.testf.utils.Json;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public final class RestExceptionHandler {

    @ExceptionHandler(value = {TestNotFoundException.class})
    public ResponseEntity testNotFound(TestNotFoundException ex, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).buildMap());
    }

    @ExceptionHandler(value = {QuestionNotFoundException.class})
    public ResponseEntity questionNotFound(QuestionNotFoundException ex, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).buildMap());
    }

    @ExceptionHandler(value = {AnswerNotFoundException.class})
    public ResponseEntity answerNotFound(AnswerNotFoundException ex, WebRequest request) {
        return status(HttpStatus.BAD_REQUEST).body(Json.builder().put("error", ex.getMessage()).buildMap());
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