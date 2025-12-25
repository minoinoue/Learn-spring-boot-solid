package com.example.learn_spring_framework.controller;

import java.util.List;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;

@RestController
@RequestMapping("/api/students") //URL chung cho controller
public class StudentController {
	
	@Autowired
	private StudentService service;

	// http://localhost:8080/api/students
	@GetMapping
	public List<Student> showStudent() {
		System.out.println("Danh sách sinh viên hiện tại: ");
		List<Student> dsach = service.getAllStudent();
		if (dsach.isEmpty()) {
			System.out.println("Danh sách hiện tại không có sinh viên nào!");
		} else {
			for(Student s : dsach) {
				System.out.println(s);
			}
		}
		return dsach; 
	}
	 
    // http://localhost:8080/api/students/add_student
    @PostMapping("/add_student")
	public void addStudent(@RequestBody Student student) {
		try {
			service.add(student.getId(), student.getFullName());
			System.out.println("Thêm sinh viên mới thành công!");
		} catch (RuntimeException e) {
			System.out.println("Lỗi không thêm được sinh viên!" + e);
		}
	}
	
    //http://localhost:8080/api/students/1?name=...
    @PutMapping("/{Id}")
	public void modifyStudent(@PathVariable String Id, @RequestParam String name) {
		try {
			service.modify(Id, name);
			System.out.println("Cập nhật sinh viên mới thành công");
		} catch (RuntimeException e) {
			System.out.println("Lỗi không cập nhật được sinh viên! " + e);
		}
	}
	
	
	// http://localhost:8080/api/students/1
	@GetMapping("/{Id}")
	public void findStudent(@PathVariable String Id) {
		try {
			System.out.println("Tìm thấy sinh viên! " + service.getById(Id));
		} catch (RuntimeException e) {
			System.out.println("Lỗi không tìm thấy sinh viên!" + e);
		}
	}
}
