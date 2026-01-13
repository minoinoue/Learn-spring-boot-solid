package com.example.learn_spring_framework.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.Student;

public interface IStudentService {
	
	Page<Student> getAllStudent(int page, int size, String sortBy, String sortDir);
	
	Page<Student> getAllContainingStudent(int page, int size, String keyword, String sortBy,  String sortDir);
	
	Student getById(String studentId);
	
	Student add(AddStudentRequest dtoStu);
	
	Student modify(String studentId, ModifyStudentRequest dtoMod);
	
	List<Student> getBin();
	
	void delete(String studentId);
	
	void deleteAll();
	
	void restore(String studentId);
}
