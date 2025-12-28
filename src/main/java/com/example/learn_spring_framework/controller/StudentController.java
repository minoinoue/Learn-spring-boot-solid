package com.example.learn_spring_framework.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;

import jakarta.validation.Valid;

//ResquestBody dùng cho put hoặc post
@RestController
@RequestMapping("/api/students") //URL chung cho controller
public class StudentController {
	
	private final StudentService service;
	
	@Autowired
	public StudentController(StudentService service) {
		this.service = service;
	}

	// http://localhost:8080/api/students GET
	@GetMapping
	public List<Student> showStudent() {
		List<Student> dsach = service.getAllStudent();
		return dsach; 
	}
	
	// http://localhost:8080/api/students/1 GET
	@GetMapping("/{id}")
	//Valid để kích hoạt validation, dùng cho các dto
	public Student findStudent(@PathVariable String id) {
			Student findStudentById = service.getById(id);
			return findStudentById;
	}
	
    // http://localhost:8080/api/students/add_student POST
    @PostMapping("/add_student")
	public ResponseEntity<String> addStudent(@RequestBody @Valid AddStudentRequest dtoStu) {
			service.add(dtoStu);
			return ResponseEntity.status(HttpStatus.CREATED).body("Thêm sinh viên mới thành công!");
			//Xử lý response trong Controller.
	}
	
    //http://localhost:8080/api/students/1 PUT
    //Truyền body newStudentName JSON
    //Truyền param key là name còn value là tên mới của mình
    @PutMapping("/{id}")
	public ResponseEntity<String> modifyStudent(@PathVariable String id, 
												@RequestBody @Valid ModifyStudentRequest dtoMod) {
			service.modify(id, dtoMod);
			return ResponseEntity.status(HttpStatus.OK).body("Cập nhật sinh viên " + id + " thành công");
	}
    
    //http://localhost:8080/api/students/1 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable String id) {
    		service.delete(id);
    		return ResponseEntity.status(HttpStatus.OK).body("Đã xóa sinh viên với id = " + id + " thành công");
    }
    
    //http://localhost:8080/api/students/ DELETE
    @DeleteMapping
    public ResponseEntity<String> deleteAllStudent() {
    		service.deleteAll();
    		return ResponseEntity.status(HttpStatus.OK).body("Đã xóa danh sách sinh viên thành công");
    }
}