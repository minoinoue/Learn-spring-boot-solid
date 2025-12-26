package com.example.learn_spring_framework.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.repository.IStudentWriteable;
import com.example.learn_spring_framework.repository.StudentRepository;
import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.IStudentReadable;

@Repository
public class StudentImpl implements IStudentWriteable, IStudentReadable {
	
	@Autowired
	private StudentRepository repository;
	
	@Override
	public List<Student> findAllStudent(){
		return repository.findAll();
	}
	
	@Override
	public Student findById(String studentId){
		for (Student s : repository.findAll()) {
			if(s.getId().equals(studentId)) {
				return s;
			} 
		} 
		return null;
	}
	
	@Override
	public void add(Student student) {
		repository.save(student);
	}
	
	@Override
	public void modify(String studentId, String fullName) {
		Student s = findById(studentId);
		if (s != null) {
			s.setFullName(fullName);
			repository.save(s);
		}
	}
}
