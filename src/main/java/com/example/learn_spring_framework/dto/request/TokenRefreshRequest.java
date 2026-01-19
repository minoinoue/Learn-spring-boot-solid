package com.example.learn_spring_framework.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
		
	@NotBlank(message = "RefreshToken không được để trống")
	private String refreshToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
