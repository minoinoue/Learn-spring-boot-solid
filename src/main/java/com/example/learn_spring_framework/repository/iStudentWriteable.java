package com.example.learn_spring_framework.repository;

import com.example.learn_spring_framework.model.Student;

public interface iStudentWriteable {
	void modify(String msv, String fullName);
	void add(Student student);
}
