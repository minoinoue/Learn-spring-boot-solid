package com.example.learn_spring_framework.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScannerConfig {
	@Bean
	public Scanner scanner() {
		return new Scanner(System.in);
	}
}

/*@Configuration: Đánh dấu một lớp cấu hình, đóng vai trò chứa các phương thức tạo ra các Bean
 * Khi Spring Boot khởi chạy, nó sẽ quét các class có gắn @Configuration 
 * để thực hiện các cài đặt hệ thống hoặc khởi tạo các đối tượng phức tạp trước khi ứng dụng đi vào hoạt động.
 * 
 * @Bean: Đặt trên các method bên trong lớp
 * Yêu cầu Spring khởi tạo đối tượng trả về từ phương thức đó và 
 * đưa vào container quản lý
 *
 *Sử dụng khi:
 *- Cấu hình các thư viện bên ngoài mà không thể thêm @Component vào code của họ 
 */