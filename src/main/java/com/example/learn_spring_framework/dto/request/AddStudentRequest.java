package com.example.learn_spring_framework.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/* DTO just serves as a data-carrying object classes to transport 
 * information between client and server and between beans but it just
 * puts necessary field in there
 * 
 * DTO goes with @RequestBody, uses for PUT method and POST method for most.
 */

public class AddStudentRequest {
	
	//Validation goes with @Value in controller to check the condition.
	@NotBlank(message = "Mã sinh viên không được để trống.")
	@Size(min = 3, max = 10, message = "Mã sinh viên phải từ 3 đến 10 kí tự.")
	private String newStudentId;
	
	@NotBlank(message = "Tên sinh viên không được để trống.")
	private String newStudentName;
	
	public String getNewStudentId() {
		return newStudentId;
	}
	public void setNewStudentId(String newStudentId) {
		this.newStudentId = newStudentId;
	}
	public String getNewStudentName() {
		return newStudentName;
	}
	public void setNewStudentName(String newStudentName) {
		this.newStudentName = newStudentName;
	}
}