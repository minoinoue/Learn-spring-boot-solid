package com.example.learn_spring_framework.repository;

import java.util.List;
import com.example.learn_spring_framework.model.Student;

public interface iStudentReadable {
	List<Student> findAllStudent();
	Student findByMsv(String msv);
}
