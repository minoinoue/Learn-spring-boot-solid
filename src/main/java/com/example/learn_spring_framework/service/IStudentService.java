package com.example.learn_spring_framework.service;

import java.util.List;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.Student;

public interface IStudentService {
	
	List<Student> getAllStudent();
	
	Student getById(String studentId);
	
	Student add(AddStudentRequest dtoStu);
	
	Student modify(String studentId, ModifyStudentRequest dtoMod);
	
	List<Student> getBin();
	
	void delete(String studentId);
	
	void deleteAll();
	
	void restore(String studentId);
}
