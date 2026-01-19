package com.example.learn_spring_framework.dto.response;

public class TokenRefreshResponse {
	
	private String tokenType = "Bearer";
	private String newAccessToken;
	private String newRefreshToken;
	
	public TokenRefreshResponse() {
		
	}
	
	public TokenRefreshResponse(String newAccessToken, String newRefreshToken) {
        this.newAccessToken = newAccessToken;
        this.newRefreshToken = newRefreshToken;
    }
	
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getNewAccessToken() {
		return newAccessToken;
	}
	public void setNewAccessToken(String accessToken) {
		this.newAccessToken = accessToken;
	}
	public String getNewRefreshToken() {
		return newRefreshToken;
	}
	public void setNewRefreshToken(String refreshToken) {
		this.newRefreshToken = refreshToken;
	}
}
