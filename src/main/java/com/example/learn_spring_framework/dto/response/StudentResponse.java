package com.example.learn_spring_framework.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StudentResponse<T> { //This class can hold every type of Object
	
	//format type of date time
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime timestamp;
	private int status;
	private String message;
	private T data; 
	
	/*T acts as a placeholder for the actual type
	 *E for elements in collections
	 *K for keys
	 *V for values
	 */
	
	public StudentResponse (LocalDateTime timestamp, int status, String message) {
		this.timestamp = timestamp;
		this.message = message;
		this.status = status;
	}
	
	public StudentResponse (LocalDateTime timestamp, int status ,String message, T data) {
		this.timestamp = timestamp;
		this.message = message;
		this.status = status;
		this.data = data;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
