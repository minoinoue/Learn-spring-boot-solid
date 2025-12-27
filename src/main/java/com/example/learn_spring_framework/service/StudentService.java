package com.example.learn_spring_framework.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.IStudentRepository;

import jakarta.persistence.NoResultException;


@Service
public class StudentService {
	
	private final IStudentRepository repo;
	
	@Autowired
	public StudentService (IStudentRepository repo) {
		this.repo = repo;
	}
	
	public List<Student> getAllStudent(){
		List<Student> allStudent = repo.findAll();
		if (allStudent.isEmpty())
			throw new NoResultException("Danh sách hiện tại không có sinh viên nào!");
		return allStudent;
	}
	
	public Student getById(String studentId) {
		Optional<Student> getStudent = repo.findById(studentId);
		if(studentId == null || studentId.trim().isEmpty()) 
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(getStudent.isEmpty())
			throw new NoResultException("Không tìm thấy sinh viên " + studentId + " trong danh sách.");
		return getStudent.get(); //get để mở hộp
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
	
	public void modify(String studentId, ModifyStudentRequest dtoMod) {
		String newFullName = dtoMod.getNewStudentName();
		if(studentId == null || studentId.trim().isEmpty())
			throw new IllegalArgumentException("Mã sinh viên không được rỗng");
		if(newFullName == null || newFullName.trim().isEmpty())
			throw new IllegalArgumentException("Tên không được rỗng!");
		Student existingStudent = getById(studentId);
		existingStudent.setFullName(newFullName);
		repo.save(existingStudent);
	}
	
	public void delete(String studentId) {
		Student existingStudent = getById(studentId);
		repo.delete(existingStudent);
	}
	
	public void deleteAll() {
		repo.deleteAll();
	}
}