package com.example.learn_spring_framework.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.model.User;

public interface IUserService {

	LoginResponse login(LoginRequest loginDto);
	
	User createStudentUser(String userName, String password);
	
	User modifyUser(User user, String newUserName, String newPassword);
	
	UserDetails loadUserByUsername(String username);
}
