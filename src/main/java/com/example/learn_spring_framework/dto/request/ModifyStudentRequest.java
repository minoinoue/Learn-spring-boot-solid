package com.example.learn_spring_framework.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ModifyStudentRequest {
	
	@NotBlank(message = "Tên sinh viên không được để trống.")
	private String newStudentName;
	
	public String getNewStudentName() {
		return newStudentName;
	}

	public void setNewStudentName(String newStudentName) {
		this.newStudentName = newStudentName;
	}	
}

