package com.example.learn_spring_framework.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/* DTO just serves as a data-carrying object classes to transport 
 * information between client and server and between beans but it just
 * puts necessary field in there
 * 
 * DTO goes with @RequestBody, uses for PUT method and POST method for most.
 */
@JsonInclude(Include.NON_NULL) //Ignore all null fields (put before the class)
public class AddStudentRequest {
	
	//Validation goes with @Value in controller to check the condition.
	@NotBlank(message = "Mã sinh viên không được để trống.")
	@Size(min = 3, max = 10, message = "Mã sinh viên phải từ 3 đến 10 kí tự.")
	private String newStudentId;
	
	@NotBlank(message = "Tên sinh viên không được để trống.")
	private String newStudentName;
	
	@NotBlank(message = "Tên đăng nhập không được để trống.")
	private String userName;
	
	@NotBlank(message = "Mật khẩu không được để trống.")
	@Size(min = 6, message = "Mật khẩu không được dưới 6 ký tự.")
	private String password;
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}