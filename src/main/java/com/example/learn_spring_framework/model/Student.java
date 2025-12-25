package com.example.learn_spring_framework.model;

public class Student {
	private String studentId;
	private String fullName;
	
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
