package com.hospital.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    private final String errorCode;

    public JwtAuthenticationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public JwtAuthenticationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}