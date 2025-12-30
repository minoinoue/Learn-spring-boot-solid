package com.example.learn_spring_framework.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ModifyStudentRequest {
	
	@NotBlank(message = "Tên sinh viên không được để trống.")
	private String newStudentName;
	
	public String getNewStudentName() {
		return newStudentName;
	}
	
	@NotBlank(message = "Tên đăng nhập không được để trống")
	private String newUserName;
	
	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, message = "Mật khẩu không được dưới 6 ký tự.")
	private String newPassword;

	public void setNewStudentName(String newStudentName) {
		this.newStudentName = newStudentName;
	}

	public String getNewUserName() {
		return newUserName;
	}

	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}	
}

