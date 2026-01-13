package com.example.learn_spring_framework.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.service.IUserService;
import com.example.learn_spring_framework.service.impl.StudentServiceImpl;

import jakarta.persistence.NoResultException;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
	
	@Mock private IStudentRepository stuRepo;
	
	@Mock private IUserService userService;
	
	@InjectMocks 
	private StudentServiceImpl stuService;
	
	@Test
	@DisplayName("Test getAllStudent: must return a Page hold a list of students")
	void getAllStudent_whenExists_shouldReturnPageOfStudent() {
		//Arrange
		Page<Student> mockPage = new PageImpl<>(java.util.Collections.emptyList());
		
		when(stuRepo.findAllByDeletedFalse(any(Pageable.class))).thenReturn(mockPage);
		
		//Act
		Page<Student> result = stuService.getAllStudent(1, 5, "fullName", "asc");
		
		//Assert
		assertThat(result).isNotNull();
		
		verify(stuRepo, times(1)).findAllByDeletedFalse(any(Pageable.class));
	}
	
	@Test
    @DisplayName("Test getById: must return student when id is existed")
	void getById_whenExists_shouldReturnStudent() {
		// Arrange
		String id = "SV001";
		Student mockStudent = new Student();
		mockStudent.setStudentId(id);
		mockStudent.setFullName("Phùng Tuấn Đạt");

		when(stuRepo.findByStudentIdAndDeletedFalse(id)).thenReturn(Optional.of(mockStudent));
		
		// Act
		Student result = stuService.getById("SV001");
		
		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFullName()).isEqualTo("Phùng Tuấn Đạt");
        verify(stuRepo).findByStudentIdAndDeletedFalse(id); // Check if repo was called
	}
	
	@Test
	@DisplayName("Test getByID: not found student must return NoResultException")
	void getById_whenNotExist_shouldReturnNotFound() {
		String id = "SV999";
		
		when(stuRepo.findByStudentIdAndDeletedFalse(id)).thenReturn(Optional.empty());
		
		assertThrows(NoResultException.class, () -> {
			stuService.getById(id);
		});
	}
	
	@Test
	@DisplayName("Test add: Add student successfully")
	void addStudent_shouldSaveStudentSuccessfully() {
		AddStudentRequest request = new AddStudentRequest();
		request.setNewStudentId("SV002");
		request.setNewStudentName("Huỳnh Thanh Phong");
		request.setPassword("htp@123");
		request.setUserName("htp");
		
		User mockUser = new User();
		mockUser.setUserName(request.getUserName());
		
		Student mockStudent = new Student(request.getNewStudentId(), request.getNewStudentName(), mockUser);
		
		when(stuRepo.existsById("SV002")).thenReturn(false);
		
		when(userService.createStudentUser("htp", "htp@123")).thenReturn(mockUser);
		
		when(stuRepo.save(any(Student.class))).thenReturn(mockStudent);
		
		Student student = stuService.add(request);
		
		assertThat(student).isNotNull();
		assertThat(student.getStudentId()).isEqualTo("SV002");
		
		verify(userService).createStudentUser("htp", "htp@123");
		
		verify(stuRepo).save(any(Student.class));
	}
	
	@Test
	@DisplayName("Test add: exists id must return IllegalStateException")
	void addStudent_whenExistsId_shouldReturnIllegalStateException() {
		AddStudentRequest request = new AddStudentRequest();
		request.setNewStudentId("SV002");
		request.setNewStudentName("Huỳnh Thanh Phong");
		request.setPassword("htp@123");
		request.setUserName("htp");
		
		User mockUser = new User();
		mockUser.setUserName(request.getUserName());
		
		when(stuRepo.existsById("SV002")).thenReturn(true);
		
		assertThrows(IllegalStateException.class, () -> {
			stuService.add(request);
		});
		
	}
	
	@Test
	@DisplayName("Test modify: Modify student information successfully")
	void modifyStudent_shouldModifySuccesfully() {
		String id = "SV001";
		ModifyStudentRequest req = new ModifyStudentRequest();
		req.setNewStudentName("Ten moi");
		
		User mockUser = new User();
		Student student = new Student(id, "Ten cu", mockUser);
		
		when(stuRepo.findByStudentIdAndDeletedFalse(id)).thenReturn(Optional.of(student));
		when(stuRepo.save(any(Student.class))).thenReturn(student);
	
		Student result = stuService.modify(id, req);
		
		assertThat(result.getFullName()).isEqualTo("Ten moi");
		verify(stuRepo).save(any(Student.class));
	}
	
	@Test
	@DisplayName("Test delete: Soft deleted must be worked by set deleted = true")
	void deletedStudent_shouldSetDeletedFalse() {
		String id = "SV001";
		Student student = new Student();
		student.setDeleted(false);
		student.setFullName("A");
		student.setStudentId(id);
		
		when(stuRepo.findByStudentIdAndDeletedFalse(id)).thenReturn(Optional.of(student));
		
		stuService.delete(id);
		
		verify(stuRepo).save(any(Student.class));
	}
}
