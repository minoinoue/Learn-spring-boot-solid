package com.example.learn_spring_framework.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.dto.response.ApiResponse;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;
import com.example.learn_spring_framework.service.UserService;

import jakarta.validation.Valid;

/*
 * To receive login/registration requests from users, 
 * call the relevant services to process them, and return the result (token).
 * 
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final StudentService stuService;
	private final UserService userService;
	
	@Autowired
	public AuthController(StudentService stuService, UserService userService) {
		this.userService = userService;
		this.stuService = stuService;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest logindto) {
		
		LoginResponse responseBody = userService.login(logindto);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Student>> registerUser(@RequestBody @Valid AddStudentRequest dtoStu) {

		Student newStudent = stuService.add(dtoStu);
				
		ApiResponse<Student> responseBody = new ApiResponse<Student>
																	(LocalDateTime.now(), 
																	201, 
																	"Thêm sinh viên mới thành công!",
																	newStudent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}
}
