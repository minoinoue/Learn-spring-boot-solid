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

/*@Service is use for classes that handle business logic code and throw 
 * exceptions.
 */
@Service
public class StudentService {
	
	private final IStudentRepository repo;
	
	/* Autowired is annotation for dependency injection to connect beans
	 * 
	 * When you define beans, because bean was only be created by once so
	 * when one class request a bean object, @Autowired will take an available
	 * object to inject into method. 
	 * 
	 * Note: Bean A depends on Bean B -> Bean B will be created first.
	 * 
	 * Order: 
	 * -> JpaRepo proxy (Spring Data created) 
	 * -> Repository bean (proxy) 
	 * -> Service -> Controller -> SpringApplicationApp.run
	 */
	@Autowired
	public StudentService (IStudentRepository repo) {
		this.repo = repo;
	}
	
	public List<Student> getAllStudent(){
		List<Student> allStudent = repo.findAll();
		return allStudent;
	}
	
	public Student getById(String studentId) {
		Optional<Student> getStudent = repo.findById(studentId);
		if(getStudent.isEmpty())
			throw new NoResultException("Không tìm thấy sinh viên " + studentId + " trong danh sách.");
		return getStudent.get(); //get the object in Optional's box
	}
	
	public Student add(AddStudentRequest dtoStu) {
		String newStudentId = dtoStu.getNewStudentId();
		String newStudentName = dtoStu.getNewStudentName();
		Student newStudent = new Student(newStudentId, newStudentName);
		if(repo.existsById(newStudentId))
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		repo.save(newStudent);
		return newStudent;
	}
	
	public void modify(String studentId, ModifyStudentRequest dtoMod) {
		String newFullName = dtoMod.getNewStudentName();
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