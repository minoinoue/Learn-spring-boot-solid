package com.example.learn_spring_framework.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.IStudentReadable;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.repository.IStudentWriteable;

@Service
public class StudentService {
	
	@Autowired 
	private IStudentRepository repo;
	
//	private final IStudentReadable reader; 
//	private final IStudentWriteable writer;
//	
//	@Autowired
//	public StudentService(IStudentReadable reader, IStudentWriteable writer){
//		this.reader = reader;
//		this.writer = writer;
//	}
	
	public List<Student> getAllStudent(){
		return repo.findAll();
	}
	
	/*
	 * Container object Optional<Student>
	 * 
	 */
	public Student getById(String studentId) {
		Optional<Student> getStudent = repo.findById(studentId);
		if(studentId == null || studentId.trim().isEmpty()) 
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(getStudent.isEmpty())
			throw new IllegalStateException("Không tìm thấy sinh viên với mã sinh viên " + studentId);
		return getStudent.get(); //get để mở hộp
	}
	
	public void modify(String studentId, String fullName) {
		if(studentId == null || studentId.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(fullName == null || fullName.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");		
		repo.save(new Student(studentId, fullName));
	}
	
	public void add(AddStudentRequest dtoStu) {
		String newStudentId = dtoStu.getNewStudentId();
		String newStudentName = dtoStu.getNewStudentName();
		if(newStudentId == null || newStudentId.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(newStudentName == null || newStudentName.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");
		if(repo.existsById(newStudentId))
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		repo.save(new Student(newStudentId, newStudentName));
	}
}