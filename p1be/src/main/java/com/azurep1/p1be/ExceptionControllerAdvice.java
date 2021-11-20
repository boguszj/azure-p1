package com.azurep1.p1be;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Optional;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public String on(EntityNotFoundException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public String on(ConstraintViolationException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    public ResponseEntity<String> on(ValidationException e) {
        return handleExceptionWithCause(e);
    }

    private ResponseEntity<String> handleExceptionWithCause(Throwable e) {
        Throwable cause = e.getCause();

        if (cause == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        } else {
            return ResponseEntity
                    .status(getStatus(cause))
                    .body(cause.getMessage());
        }
    }

    private HttpStatus getStatus(Throwable cause) {
        return Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(cause.getClass(), ResponseStatus.class))
                .map(ResponseStatus::code)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
