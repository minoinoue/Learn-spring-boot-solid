package com.example.learn_spring_framework.exception;

import org.springframework.security.core.AuthenticationException;

/*Purpose: To consolidate all the convoluted errors in the JWT library 
 * (expired, incorrect signature, incorrect formatting, etc.) 
 * into a single error so that the filter can easily catch and process them.
 * 
 */

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