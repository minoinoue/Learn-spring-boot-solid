package com.example.learn_spring_framework.dto.response;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {
	
	// pattern: Quy định định dạng bạn muốn (dd là ngày, MM là tháng, yyyy là năm)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime timestamp; //Thời gian có lỗi
	private int status; //kiểu lỗi
	private String message; //thông báo trả về

	@Autowired
	public ErrorResponse(int status, LocalDateTime timestamp, String message) {
		this.status = status;
		this.timestamp = timestamp;
		this.message = message;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
