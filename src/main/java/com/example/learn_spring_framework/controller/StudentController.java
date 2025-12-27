package com.example.learn_spring_framework.controller;

import java.util.List;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.config.ScannerConfig;
import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;

@RestController
@RequestMapping("/api/students") //URL chung cho controller
public class StudentController {
	
	private final StudentService service;
	
	@Autowired
	public StudentController(StudentService service) {
		this.service = service;
	}

	// http://localhost:8080/api/students
	@GetMapping
	public List<Student> showStudent() {
		List<Student> dsach = service.getAllStudent();
		return dsach; 
	}
	
	// http://localhost:8080/api/students/1
	@GetMapping("/{Id}")
	public Student findStudent(@PathVariable String Id) {
			Student findStudentById = service.getById(Id);
			return findStudentById;
	}
	 
    // http://localhost:8080/api/students/add_student
    @PostMapping("/add_student")
	public String addStudent(@RequestBody AddStudentRequest dtoStu) {
			service.add(dtoStu);
			return "Thêm sinh viên mới thành công!";
	}
	
    //http://localhost:8080/api/students/1?newStudentName=...
  //Truyền param key là name còn value là tên mới của mình
    @PutMapping("/{Id}")
	public String modifyStudent(@PathVariable String Id, ModifyStudentRequest dtoMod) {
			service.modify(Id, dtoMod);
			return "Cập nhật sinh viên " + Id + " thành công";
	}
    
    //http://localhost:8080/api/students/1233 DELETE
    @DeleteMapping("/{Id}")
    public String deleteStudent(@PathVariable String Id) {
    		service.delete(Id);
    		return "Đã xóa sinh viên với id = " + Id + " thành công";
    }
    
    //http://localhost:8080/api/students/ DELETE
    @DeleteMapping
    public String deleteAllStudent() {
    		service.deleteAll();
    		return "Đã xóa danh sách sinh viên thành công.";
    }
}