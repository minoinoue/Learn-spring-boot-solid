package com.example.learn_spring_framework.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
	
	private Student mockStudent;
	private User mockUser;
	
	@BeforeEach
	void setUp() {
		mockUser = new User();
		mockUser.setId(1L);
		mockUser.setUserName("user_test");
		
		mockStudent = new Student("SV001", "Phùng Tuấn Đạt", mockUser);
		mockStudent.setDeleted(false);
	}
	
	@Test
	@DisplayName("Test getAllStudent: must return a list of students")
	void getAllStudent_whenExists_shouldReturnStudents() {
		//Arrange
		Page<Student> mockPage = new PageImpl<>(List.of(mockStudent));
		//return Page result from database
		
		when(stuRepo.findAllByDeletedFalse(any(Pageable.class))).thenReturn(mockPage);
		
		//Act
		Page<Student> result = stuService.getAllStudent(1, 5, "fullName", "asc");
		
		//Assert
		assertThat(result).isNotNull();
		assertEquals(1, result.getNumberOfElements());
		verify(stuRepo, times(1)).findAllByDeletedFalse(any(Pageable.class));
	}
	
	@Test
    @DisplayName("Test getById: must return student when id is existed")
	void getById_whenExists_shouldReturnStudent() {

		when(stuRepo.findByStudentIdAndDeletedFalse("SV001")).thenReturn(Optional.of(mockStudent));
		
		// Act
		Student result = stuService.getById("SV001");
		
		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getFullName()).isEqualTo("Phùng Tuấn Đạt");
        verify(stuRepo).findByStudentIdAndDeletedFalse("SV001"); // Check if repo was called
	}
	
	@Test
	@DisplayName("Test getByID: not found student must return NoResultException")
	void getById_whenNotExist_shouldReturnNotFound() {
		
		when(stuRepo.findByStudentIdAndDeletedFalse("SV002")).thenReturn(Optional.empty());
		
		assertThrows(NoResultException.class, () -> {
			stuService.getById("SV002");
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
		
		when(stuRepo.existsById("SV002")).thenReturn(false);
		
		when(userService.createStudentUser("htp", "htp@123")).thenReturn(mockUser);
		
		when(stuRepo.save(any(Student.class))).thenReturn(new Student("SV002", "Huỳnh Thanh Phong", mockUser));
		
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
		
		when(stuRepo.existsById("SV002")).thenReturn(true);
		
		assertThrows(IllegalStateException.class, () -> {
			stuService.add(request);
		});
		
	}
	
	@Test
	@DisplayName("Test modify: Modify student information successfully")
	void modifyStudent_shouldModifySuccesfully() {
		ModifyStudentRequest req = new ModifyStudentRequest();
		req.setNewStudentName("Uất Quỳnh Nga");
		req.setNewUserName("uqn");
		req.setNewPassword("uqn@123");
		
		when(stuRepo.findByStudentIdAndDeletedFalse("SV001")).thenReturn(Optional.of(mockStudent));
		when(stuRepo.save(any(Student.class))).thenReturn(mockStudent);
	
		Student result = stuService.modify("SV001", req);
		
		assertThat(result.getFullName()).isEqualTo("Uất Quỳnh Nga");
		verify(userService, times(1)).modifyUser(any(User.class), anyString(), anyString());
		verify(stuRepo).save(any(Student.class));
	}
	
	@Test
	@DisplayName("Test delete: Soft deleted must be worked by set deleted = true")
	void deletedStudent_shouldSetDeletedFalse() {
		
		when(stuRepo.findByStudentIdAndDeletedFalse("SV001")).thenReturn(Optional.of(mockStudent));
		
		stuService.delete("SV001");
		
		assertTrue(mockStudent.isDeleted());
		verify(stuRepo).save(any(Student.class));
	}
	
	@Test
	@DisplayName("Restore student: Restore Successfully")
	void restoreStudent_shouldSetDeletedTrue() {
		mockStudent.setDeleted(true);
		
		when(stuRepo.findStudentByStudentIdAndDeleted("SV001", true)).thenReturn(Optional.of(mockStudent));
		
		stuService.restore("SV001");
		
		assertFalse(mockStudent.isDeleted());
		verify(stuRepo).save(mockStudent);
	}
	
	@Test
	@DisplayName("Restore student: Not found student in bin")
	void restoreStudent_notFoundInBin_ShouldThrowNoResultException() {
		
		when(stuRepo.findStudentByStudentIdAndDeleted("SV001", true)).thenReturn(Optional.empty());
		
		assertThrows(NoResultException.class, () -> {
			stuService.restore("SV001");
		});
	}
	
	@Test
	@DisplayName("Get deleted students in bin successfully!")
	void getBin_shouldReturnDeletedStudents() {
		when(stuRepo.findAllByDeletedTrue()).thenReturn(List.of(mockStudent));
		
		List<Student> bin = stuService.getBin();
		
		assertFalse(bin.isEmpty());
		verify(stuRepo).findAllByDeletedTrue();
	}
	
	@Test
	@DisplayName("Deleted all student successfully")
	void deletedAllStudent_shouldSuccessfully() {
		
		stuService.deleteAll();
		
		verify(stuRepo).softDeleteAllStudents();
	}
}
