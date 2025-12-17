package org.recruitmenttask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidHourCountException.class)
    public ErrorResponse handleInvalidHourCount(InvalidHourCountException exception) {
        return ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleInvalidArgumentException(IllegalArgumentException exception) {
        return ErrorResponse.create(exception, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({RestClientException.class, ServiceIsUnavailableException.class})
    public ErrorResponse handleRestClientError(RuntimeException exception) {
        return ErrorResponse.create(exception, HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
