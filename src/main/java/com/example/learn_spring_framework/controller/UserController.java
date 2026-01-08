package com.example.learn_spring_framework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;

import com.example.learn_spring_framework.service.IUserService;


/*
 * To receive login/registration requests from users, 
 * call the relevant services to process them, and return the result (token).
 * 
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	private final IUserService userService;
	
	@Autowired
	public UserController(IUserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest logindto) {
		
		LoginResponse responseBody = userService.login(logindto);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}
}
