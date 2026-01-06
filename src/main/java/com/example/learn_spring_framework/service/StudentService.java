package com.example.learn_spring_framework.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.ERole;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IRoleRepository;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.repository.IUserRepository;

import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;

/*@Service is use for classes that handle business logic code and throw 
 * exceptions.
 */
@Service
public class StudentService {
	
	private final IStudentRepository stuRepo;
	private final UserService userService;
	
	
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
	public StudentService (UserService userService, IStudentRepository stuRepo) {
		this.stuRepo = stuRepo;
		this.userService = userService;
	}
	
	public List<Student> getAllStudent(){
		List<Student> allStudent = stuRepo.findAll();
		return allStudent;
	}
	
	public Student getById(String studentId) {
		Optional<Student> getStudent = stuRepo.findById(studentId);
		if(getStudent.isEmpty())
			throw new NoResultException("Không tìm thấy sinh viên " + studentId + " trong danh sách.");
		return getStudent.get(); //get the object in Optional's box
	}
	
	@Transactional
	public Student add(AddStudentRequest dtoStu, User savedUser) {
		
		String newStudentId = dtoStu.getNewStudentId();
		if(stuRepo.existsById(newStudentId))
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		
		String newStudentName = dtoStu.getNewStudentName();
        
		Student newStudent = new Student(newStudentId, newStudentName, savedUser);
		
		return stuRepo.save(newStudent);
	}
	
	@Transactional
	public Student modify(String studentId, ModifyStudentRequest dtoMod) {
		String newStudentName = dtoMod.getNewStudentName();
		String newStudentUserName = dtoMod.getNewUserName();
		String newStudentPassword = dtoMod.getNewPassword();
		Student existingStudent = getById(studentId);
		existingStudent.setFullName(newStudentName);
		User modUser = existingStudent.getUser();
		if (modUser != null) {
			userService.modifyUser(modUser, newStudentUserName, newStudentPassword);
		}
		return stuRepo.save(existingStudent);
	}
	
	public List<Student> getBin(){
		return stuRepo.findAllDeletedStudent();
	}
	
	@Transactional
	public void delete(String studentId) {
		Student existingStudent = getById(studentId);
		existingStudent.setDeleted(true);
		stuRepo.save(existingStudent);
	}
	
	public void deleteAll() {
		stuRepo.deleteAll();
	}
	
	@Transactional
	public void restore(String studentId) {
		Student deletedStudent = stuRepo.findDeletedStudentById(studentId);
				deletedStudent.setDeleted(false);
				stuRepo.save(deletedStudent);
	}
}