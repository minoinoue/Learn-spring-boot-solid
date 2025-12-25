package com.example.learn_spring_framework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.IStudentReadable;
import com.example.learn_spring_framework.repository.IStudentWriteable;

@Service
public class StudentService {
	
	private final IStudentReadable reader; 
	private final IStudentWriteable writer;
	
	@Autowired
	public StudentService(IStudentReadable reader, IStudentWriteable writer){
		this.reader = reader;
		this.writer = writer;
	}
	
	public List<Student> getAllStudent(){
		return reader.findAllStudent();
	}
	
	public Student getById(String studentId) {
		Student getStudent = reader.findById(studentId);
		if(studentId == null || studentId.trim().isEmpty()) 
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(getStudent == null)
			throw new IllegalStateException("Không tìm thấy sinh viên với mã sinh viên " + studentId);
		return getStudent;
	}
	
	public void modify(String studentId, String fullName) {
		if(studentId == null || studentId.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(fullName == null || fullName.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");		
		writer.modify(studentId, fullName);
	}
	
	public void add(String studentId, String fullName) {
		Student getStudent = reader.findById(studentId);
		if(studentId == null || studentId.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(fullName == null || fullName.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");
		if(getStudent != null)
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		writer.add(new Student(studentId, fullName));
	}
}
