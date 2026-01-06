package com.example.learn_spring_framework.ServiceTest;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.repository.IRoleRepository;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.service.StudentService;

@ExtendWith(MockitoExtension.class) //activate Mockito
public class StudentServiceTest {
	
	@Mock //create fake object
	private IUserRepository userRepo;
	
	@Mock
	private IStudentRepository stuRepo;
	
	@Mock 
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private IRoleRepository roleRepo;
	
	@InjectMocks //inject mock into real service
	private StudentService studentService;
	
	@Test //Test case 1: Get student successful
	void getById_whenExists_shouldReturnStudent() {
		
		//Arrange
		String id = "SV001";
		Student mockStudent = new Student();
		mockStudent.setStudentId(id);
		mockStudent.setFullName("Phùng Tuấn Đạt");
		
		//teach Mock: "When someone call findById with is id, return mockStudent"
		Mockito.when(stuRepo.findById(id)).thenReturn(Optional.of(mockStudent));
		
		//act
		Student result = studentService.getById(id);
		
		//assert
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getFullName().compareTo("Phùng Tuấn Đạt"));
	}
}
