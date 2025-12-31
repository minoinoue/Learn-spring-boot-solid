package com.example.learn_spring_framework.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/*
 * In Spring Security, this class is responsible for 
 * blocking and responding to anonymous 
 * (unauthenticated) requests attempting to access 
 * protected resources.
 * 
 */
@Component
public class AuthEntryPointJwtSecurity implements AuthenticationEntryPoint {
	
	@Override
	/*This method runs automatically when the authentication process fails.
	 * 
	 */
	public void commence(
			HttpServletRequest request, //it take request, response and specific errors 
			HttpServletResponse response,
			AuthenticationException authException
			) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
		//send 401 error to Client
	}
}
