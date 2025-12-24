package com.example.learn_spring_framework.model;

public class Student {
	String msv;
	String fullName;
	
	public Student(String msv, String fullName) {
		this.msv = msv;
		this.fullName = fullName;
	}
	
	public String getMsv() {
		return msv;
	}

	public void setMsv(String msv) {
		this.msv = msv;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	@Override
	public String toString() {
		return "Mã sinh viên: " + msv + " | Họ và tên sinh viên: " + fullName;
	}

	
}
