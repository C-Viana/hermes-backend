package com.cviana.hermes.exceptions;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cviana.hermes.exceptions.errors.HermesServerErrorException;
import com.cviana.hermes.exceptions.errors.UnsupportedProviderException;
import com.cviana.hermes.exceptions.response.ResponseErrorDefault;

@RestControllerAdvice
public class HermesExceptionAdviser {

    @ExceptionHandler(HermesServerErrorException.class)
    public ResponseEntity<ResponseErrorDefault> internalServerErrorExceptions(HermesServerErrorException exception) {
        ResponseErrorDefault response = new ResponseErrorDefault(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exception.getMessage(),
            LocalDateTime.now(Clock.systemDefaultZone())
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedProviderException.class)
    public ResponseEntity<ResponseErrorDefault> unsupportedProviderException(UnsupportedProviderException exception) {
        ResponseErrorDefault response = new ResponseErrorDefault(
            HttpStatus.BAD_REQUEST.value(),
            exception.getMessage(),
            LocalDateTime.now(Clock.systemDefaultZone())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
