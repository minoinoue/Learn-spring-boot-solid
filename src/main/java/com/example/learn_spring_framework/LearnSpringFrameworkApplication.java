package com.example.learn_spring_framework;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.learn_spring_framework.controller.StudentController;

@SpringBootApplication
public class LearnSpringFrameworkApplication implements CommandLineRunner {

	@Autowired // Nhờ Spring lấy cái Controller đưa vào
    private StudentController control;
	
	public static void main(String[] args) {
		
		SpringApplication.run(LearnSpringFrameworkApplication.class, args);

	}
	
	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		int choice = 0;
		
		while(true) {
	        System.out.println("\nQUẢN LÝ SINH VIÊN");
	        System.out.println("1. Thêm sinh viên mới");
	        System.out.println("2. Tìm kiếm theo MSV");
	        System.out.println("3. Sửa tên sinh viên");
	        System.out.println("4. Xem danh sách sinh viên");
	        System.out.println("0. Thoát");
	        System.out.print("-> Mời bạn chọn (0-4): ");
	
	
	        try {
	            choice = Integer.parseInt(scanner.nextLine());
	        } catch (NumberFormatException e) {
	            System.out.println("Vui lòng nhập số!");
	            continue;
	        }
	
	        switch (choice) {
	        	case 1:
	        		control.addStudent(scanner);
	        		break;     
	        	case 2:
	        		control.findStudent(scanner);
	        		break;
	        	case 3: 
	        		control.modifyStudent(scanner);
	        		break;
	        	case 4: 
	        		control.showStudent();
	        		break;
	        	case 0:
	        		System.out.println("Kết thúc chương trình!");
	        		System.exit(0);
	        		break;
	        	default:
	        		System.out.println("Chức năng không tồn tại");
	        }
	    }
	}
}