package com.example.learn_spring_framework.dto.request;

/*- DTO Request đại diện cho những gì cta muốn người dùng cung cấp. 
 * Nó thường đi kèm với chú thích @RequestBody trong Controller.
 * 
 * - DTO Response đại diện cho những gì bạn muốn hiển thị cho người dùng. 
 * Nó bảo vệ các thông tin nhạy cảm của Entity.
 * 
 * - Dùng trong POST, PUT
 * 
 * - Vận chuyển dữ liệu giữa Client Postman và Server Controller.
 */
public class AddStudentRequest {
	
	private String newStudentId;
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