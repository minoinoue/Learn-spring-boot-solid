package com.example.learn_spring_framework.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.request.TokenRefreshRequest;
import com.example.learn_spring_framework.dto.response.ApiResponse;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.dto.response.TokenRefreshResponse;
import com.example.learn_spring_framework.mapper.IAuthMapper;
import com.example.learn_spring_framework.model.RefreshToken;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.service.IRefreshTokenService;
import com.example.learn_spring_framework.service.IUserService;

import jakarta.validation.Valid;


/*
 * To receive login/registration requests from users, 
 * call the relevant services to process them, and return the result (token).
 * 
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	private final IUserService userService;
	private final IRefreshTokenService refreshTokenService;
	private final IAuthMapper authMapper;
	
	@Autowired
	public UserController(IUserService userService, IRefreshTokenService refreshTokenService, IAuthMapper authMapper) {
		this.userService = userService;
		this.refreshTokenService = refreshTokenService;
		this.authMapper = authMapper;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest logindto) {
		
		LoginResponse responseBody = userService.login(logindto);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}
	
	@PostMapping("/signout")
	public ResponseEntity<ApiResponse<Void>> logoutUser(){
		//get information active user
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		
		User user = userService.findByUserName(userName);
	
		refreshTokenService.deleteByUserId(user.getId());
		
		ApiResponse<Void> responseBody = new ApiResponse<>(
						                LocalDateTime.now(), 
						                200, 
						                "Đăng xuất thành công!" 
						               );

		return ResponseEntity.ok(responseBody);
	}
	
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(@Valid @RequestBody TokenRefreshRequest req){
		
	    String requestToken = req.getRefreshToken(); 
 
	    RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(requestToken);
	    
	    String newAccessToken = refreshTokenService.createNewAccessToken(newRefreshToken);
	    
	    TokenRefreshResponse responseData = authMapper.toRefreshResponse(newRefreshToken.getToken(), newAccessToken);

	    ApiResponse<TokenRefreshResponse> responseBody = new ApiResponse<>(
	                                                        LocalDateTime.now(), 
	                                                        200, 
	                                                        "Làm mới token thành công.", 
	                                                        responseData);

	    return ResponseEntity.ok(responseBody);
	}
}
