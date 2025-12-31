package com.example.learn_spring_framework.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class LoginResponse {
	private LocalDateTime timestamp;
    private String token;
    private String username;
    private List<String> roles; 
    

    public LoginResponse(LocalDateTime timestamp, String token, String username, List<String> roles) {
        this.timestamp = timestamp;
    	this.token = token;
        this.username = username;
        this.roles = roles;
    }

    
    public LocalDateTime getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}