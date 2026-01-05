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
import jakarta.transaction.Transactional;

/*@Service is use for classes that handle business logic code and throw 
 * exceptions.
 */
@Service
public class StudentService {
	
	private final IUserRepository userRepo;
	private final IStudentRepository stuRepo;
	private final PasswordEncoder passwordEncoder;
	private final IRoleRepository roleRepo;
	
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
	public StudentService (IUserRepository userRepo, IStudentRepository stuRepo, PasswordEncoder passwordEncoder, IRoleRepository roleRepo) {
		this.userRepo = userRepo;
		this.stuRepo = stuRepo;
		this.passwordEncoder = passwordEncoder;
		this.roleRepo = roleRepo;
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
	public Student add(AddStudentRequest dtoStu) {
		String newStudentId = dtoStu.getNewStudentId();
		String newStudentName = dtoStu.getNewStudentName();
		String newUserName = dtoStu.getUserName();
		String newPassword = dtoStu.getPassword();
		String newPasswordEncoder = (passwordEncoder.encode(newPassword));
		
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepo.findByName(ERole.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Không thấy role của người dùng."));
        roles.add(userRole);
        
		Student newStudent = new Student(newStudentId, newStudentName, new User(newUserName, newPasswordEncoder, roles));
		if(userRepo.existsByUserName(newStudentId))
			throw new IllegalStateException("Username đã tồn tại!");
		if(stuRepo.existsById(newStudentId))
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		return stuRepo.save(newStudent);
	}
	
	@Transactional
	public Student modify(String studentId, ModifyStudentRequest dtoMod) {
		String newStudentName = dtoMod.getNewStudentName();
		Student existingStudent = getById(studentId);
		existingStudent.setFullName(newStudentName);
		User linkedUser = existingStudent.getUser();
		if (linkedUser != null) {
			//Update new user name
			linkedUser.setUserName(dtoMod.getNewUserName());
			
			// Update new password
			String encodedPassword = passwordEncoder.encode(dtoMod.getNewPassword());
			linkedUser.setPassword(encodedPassword);
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
	
	
	public void restore(String studentId) {
		List<Student> deletedStudent = getBin();
		for(Student students : deletedStudent) {
			if(students.getStudentId().equals(studentId)) {
				students.setDeleted(false);
				stuRepo.save(students);
			}
		}
	}
}