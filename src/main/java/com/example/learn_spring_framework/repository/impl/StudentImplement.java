package com.example.learn_spring_framework.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.repository.iStudentWriteable;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.iStudentReadable;

@Repository
public class StudentImplement implements iStudentWriteable, iStudentReadable {
	
	List<Student> students = new ArrayList<Student>();
	
	public StudentImplement() {
		students.add(new Student("1", "A"));
		students.add(new Student("2", "B"));
	}
	
	@Override
	public List<Student> findAllStudent(){
		return students;
	}
	
	@Override
	public Student findByMsv(String msv){
		for (Student s : students) {
			if(s.getMsv().equals(msv)) {
				return s;
			} 
		} 
		return null;
	}
	
	@Override
	public void add(Student student) {
		students.add(student);
	}
	
	@Override
	public void modify(String msv, String fullName) {
		Student s = findByMsv(msv);
		if (s != null) {
			s.setFullName(fullName);
		}
	}
}
