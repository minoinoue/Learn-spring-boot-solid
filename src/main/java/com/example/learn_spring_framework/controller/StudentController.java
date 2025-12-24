package com.example.learn_spring_framework.controller;

import java.util.List;

import java.util.Scanner;

import org.springframework.stereotype.Controller;

import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;

@Controller
public class StudentController {
	
	private final StudentService service;
	
	public StudentController(StudentService service) {
		this.service = service;
	}
	
	public void showStudent() {
		System.out.println("Danh sách sinh viên hiện tại: ");
		List<Student> dsach = service.getAllStudent();
		if (dsach.isEmpty()) {
			System.out.println("Danh sách hiện tại không có sinh viên nào!");
		} else {
			for(Student s : dsach) {
				System.out.println(s);
			}
		}
	}
	
	public void addStudent(Scanner scanner) {
		System.out.println("Nhập mã sinh viên mới: ");
		String newmsv = scanner.nextLine();
		System.out.println("Nhập tên sinh viên mới: ");
		String newname = scanner.nextLine();
		try {
			service.add(newmsv, newname);
			System.out.println("Thêm sinh viên mới thành công!");
		} catch (RuntimeException e) {
			System.out.println("Lỗi không thêm được sinh viên!" + e);
		}
	}
	
	public void modifyStudent(Scanner scanner) {
		System.out.println("Nhập mã sinh viên cần sửa: ");
		String findmsv = scanner.nextLine();
		System.out.println("Nhập tên mới: ");
		String modname = scanner.nextLine();
		try {
			service.modify(findmsv, modname);
			System.out.println("Cập nhật sinh viên mới thành công");
		} catch (RuntimeException e) {
			System.out.println("Lỗi không cập nhật được sinh viên! " + e);
		}
	}
	
	public void findStudent(Scanner scanner) {
		System.out.println("Nhập mã sinh viên cần tìm: ");
		String findmsv = scanner.nextLine();
		try {
			System.out.println("Tìm thấy sinh viên" + service.getfindByMsv(findmsv));
		} catch (RuntimeException e) {
			System.out.println("Lỗi không tìm thấy sinh viên!" + e);
		}
	}
}
