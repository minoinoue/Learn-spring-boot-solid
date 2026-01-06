package com.example.learn_spring_framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;

@Service
public class RegistrationService {
	
	private final StudentService studentService;
	private final UserService userService;
	
	@Autowired
	public RegistrationService(StudentService studentService, UserService userService) {
		this.studentService = studentService;
		this.userService = userService;
	}
	
	public Student registerStudent(AddStudentRequest dtoStu) {
		
		User savedUser = userService.createUserAccount(dtoStu.getUserName(), dtoStu.getPassword());
		
		Student savedStudent = studentService.add(dtoStu, savedUser);
		
		return savedStudent;
		
	}

}
