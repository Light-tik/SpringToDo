package com.emobile.springtodo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("Validation failed: {}", errors);
        return Map.of("errors", errors);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(CustomException ex) {
        log.warn("Custom exception: {}", ex.getMessage());
        return HttpStatus.NOT_FOUND + ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return HttpStatus.INTERNAL_SERVER_ERROR + "Something went wrong";
    }
}
