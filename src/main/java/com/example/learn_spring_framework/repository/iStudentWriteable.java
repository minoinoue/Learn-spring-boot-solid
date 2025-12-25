package com.example.learn_spring_framework.repository;

import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.model.Student;

public interface IStudentWriteable {
	void modify(String studentId, String fullName);
	void add(Student student);
}
