package com.example.learn_spring_framework.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtException extends AuthenticationException {
    

	private static final long serialVersionUID = 1L;

	// Constructor just take message
    public InvalidJwtException(String msg) {
        super(msg);
    }

    public InvalidJwtException(String msg, Throwable cause) {
        super(msg, cause);
    }
}