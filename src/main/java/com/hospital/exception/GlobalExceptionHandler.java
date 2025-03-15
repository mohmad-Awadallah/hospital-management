package com.hospital.exception;

import com.hospital.security.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorDetails error = new ErrorDetails(new Date(), ex.getMessage(), "RESOURCE_NOT_FOUND");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDetails> handleDuplicateResource(DuplicateResourceException ex) {
        ErrorDetails error = new ErrorDetails(new Date(), ex.getMessage(), "DUPLICATE_RESOURCE");
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorDetails error = new ErrorDetails(new Date(), ex.getMessage(), "INVALID_CREDENTIALS");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        ErrorDetails error = new ErrorDetails(new Date(), ex.getMessage(), "JWT_AUTH_ERROR");
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralException(Exception ex) {
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred.";
        ErrorDetails error = new ErrorDetails(new Date(), errorMessage, "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
