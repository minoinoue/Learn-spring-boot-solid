package com.example.learn_spring_framework.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;

import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.service.IStudentService;
import com.example.learn_spring_framework.service.IUserService;

import jakarta.persistence.NoResultException;
import org.springframework.transaction.annotation.Transactional;

/*@Service is use for classes that handle business logic code and throw 
 * exceptions.
 */
@Service
public class StudentServiceImpl implements IStudentService{
	
	private final IStudentRepository stuRepo;
	private final IUserService userService;
	
	
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
	public StudentServiceImpl (IUserService userService, IStudentRepository stuRepo) {
		this.stuRepo = stuRepo;
		this.userService = userService;
	}
	
	@Override
	public Page<Student> getAllStudent(int page, int size, String sortBy, String sortDir){
		
		//if client send "desc" -> descending, else ascending
		Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		
		//create sort object 
		Sort sort = Sort.by(direction, sortBy);
		
		//create obj page request
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		return stuRepo.findAllByDeletedFalse(pageable);
	}
	
	@Override
	public Student getById(String studentId) {
		Optional<Student> getStudent = stuRepo.findByStudentIdAndDeletedFalse(studentId);
		if(getStudent.isEmpty())
			throw new NoResultException("Không tìm thấy sinh viên " + studentId + " trong danh sách.");
		return getStudent.get(); //get the object in Optional's box
	}
	
	@Override
	@Transactional
	public Student add(AddStudentRequest dtoStu) {
		
		if(stuRepo.existsById(dtoStu.getNewStudentId()))
            throw new IllegalStateException("Mã sinh viên đã tồn tại!");

        User newUser = userService.createStudentUser(dtoStu.getUserName(), dtoStu.getPassword());
        
        Student newStudent = new Student(dtoStu.getNewStudentId(), dtoStu.getNewStudentName(), newUser);
        return stuRepo.save(newStudent);
	}
	
	@Override
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
	
	@Override
	public List<Student> getBin(){
		return stuRepo.findAllByDeletedTrue();
	}
	
	@Override
	@Transactional
	public void delete(String studentId) {
		Student existingStudent = getById(studentId);
		existingStudent.setDeleted(true);
		stuRepo.save(existingStudent);
	}
	
	@Override
	@Transactional
	public void deleteAll() {
		stuRepo.softDeleteAllStudents();
	}
	
	@Override
	@Transactional
	public void restore(String studentId) {
		Optional<Student> deletedStudent = stuRepo.findStudentByStudentIdAndDeleted(studentId, true);
		if(deletedStudent.isEmpty())
			throw new NoResultException("Không tìm thấy sinh viên " + studentId + " trong danh sách bị xóa.");
				Student deletedStu = deletedStudent.get();
				deletedStu.setDeleted(false);
				stuRepo.save(deletedStu);
	}
}