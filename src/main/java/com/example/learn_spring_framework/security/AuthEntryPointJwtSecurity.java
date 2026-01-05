package com.example.learn_spring_framework.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.learn_spring_framework.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(
                HttpServletResponse.SC_UNAUTHORIZED, 
                LocalDateTime.now(),
                "Bạn cần đăng nhập để truy cập tài nguyên này." 
        );
        //use objectmapper to write json into response
        final ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.writeValue(response.getOutputStream(), errorResponse);
		//send 401 error to Client
	}
}
