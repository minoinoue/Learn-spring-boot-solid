package com.example.learn_spring_framework.dto.request;

public class ModifyStudentRequest {
	private String newStudentName;
	
	public String getNewStudentName() {
		return newStudentName;
	}

	public void setNewStudentName(String newStudentName) {
		this.newStudentName = newStudentName;
	}	
}
 /*Nguyên tắc "Định danh Tài nguyên" (Resource Identification)
  *URL (Uniform Resource Locator): Dùng để xác định "Cái gì" (Đối tượng nào) chịu tác động
  *Body: Dùng để chứa "Nội dung" (Dữ liệu) cần thay đổi.
  *Method (PUT): Dùng để xác định "Hành động"
  */

