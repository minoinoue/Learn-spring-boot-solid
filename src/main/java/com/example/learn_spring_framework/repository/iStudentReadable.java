package com.example.learn_spring_framework.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.model.Student;

public interface IStudentReadable {
	List<Student> findAllStudent();
	Student findById(String studentId);
}
