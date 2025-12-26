package com.example.learn_spring_framework.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students") //Tên bảng
public class Student {
	@Id
	@Column(name = "student_id")
	private String studentId; //Dùng để lưu xuống cột StudentID
	
	@Column(name = "full_name")
	private String fullName;
	
	//Hibernate yêu cầu có contrucstor không tham số
	public Student() {
		
	}
	
	public Student(String studentId, String fullName) {
		this.studentId = studentId;
		this.fullName = fullName;
	}
	
	public String getId() {
		return studentId;
	}

	public void setId(String studentId) {
		this.studentId = studentId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public String toString() {
		return "Mã sinh viên: " + studentId + " | Họ và tên sinh viên: " + fullName;
	}

	
}