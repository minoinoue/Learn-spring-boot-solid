package com.example.learn_spring_framework.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.dto.response.StudentResponse;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;
import com.example.learn_spring_framework.service.UserService;
import com.example.learn_spring_framework.util.JWTUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtils;
	private final StudentService service;
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager,
							JWTUtil jwtUtils,
							StudentService service) {
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.service = service;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest logindto) {
		
		//throw exception when you put wrong user or password
		Authentication authentication = authenticationManager.authenticate(
										new UsernamePasswordAuthenticationToken(
												logindto.getUserName(), 
												logindto.getPassword()
		));		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		String jwtToken = jwtUtils.generateToken(userDetails.getUsername());
		
		List<String> roles = userDetails.getAuthorities().stream()
	            .map(item -> item.getAuthority())
	            .collect(Collectors.toList());
		
		LoginResponse responseBody = new LoginResponse(LocalDateTime.now(),
													jwtToken,
													userDetails.getUsername(),
													roles
													);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

	@PostMapping("/signup")
	public ResponseEntity<StudentResponse<Student>> registerUser(@RequestBody @Valid AddStudentRequest dtoStu) {

		Student newStudent = service.add(dtoStu);
				
		StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(), 
				201, 
				"Thêm sinh viên mới thành công!",
				newStudent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}
}
