package com.example.learn_spring_framework.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
	
	// @NotBlank: Không được null và không được là chuỗi rỗng sau khi trim()
    // message: Thông báo lỗi sẽ hiện ra nếu vi phạm
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