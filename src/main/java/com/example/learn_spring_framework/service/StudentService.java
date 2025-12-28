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

/*Bean Validation được dùng để check rỗng.
 * Bean Validation (hay còn gọi là Jakarta Validation) là một tiêu chuẩn trong Java giúp bạn kiểm tra tính hợp lệ của dữ liệu 
 * (validate) ngay từ khi nó bước vào ứng dụng, 
 * thông qua các Annotation đơn giản thay vì viết hàng tá câu lệnh if-else lặp đi lặp lại.
 * 
 * Thay vì để service kiểm tra, DTO sẽ tự bảo vệ nó
 * @Valid
 * @NotNull
 * @Size
 * @Email
 * @BindingResult
 * 
 * Các bước:
 * - Thêm dependency starter validation
 * - Dùng annotation valid ở dto
 * - Gắn các annotation vào trước các @ResquestBody để check
 * - Loại bỏ các if else check rỗng
 * - Hứng lỗi validation MethodArgumentNotValidException
 */
@Service
public class StudentService {
	
	private final IStudentRepository repo;
	
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
		return getStudent.get(); //get để mở hộp
	}
	
	public void add(AddStudentRequest dtoStu) {
		//Bỏ các if-else logic valid ra, chỉ còn chứa logic nghiệp vụ
		String newStudentId = dtoStu.getNewStudentId();
		String newStudentName = dtoStu.getNewStudentName();
		if(repo.existsById(newStudentId))
			throw new IllegalStateException("Mã sinh viên đã tồn tại!");
		repo.save(new Student(newStudentId, newStudentName));
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