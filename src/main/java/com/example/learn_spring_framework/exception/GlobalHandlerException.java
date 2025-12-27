package com.example.learn_spring_framework.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.learn_spring_framework.dto.response.ErrorResponse;

import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

@RestControllerAdvice //Nơi xử lý lỗi chung
public class GlobalHandlerException {
	
	/*
	 * Bắt lỗi IllegalArgrumentException: tham số truyền vào rỗng
	 * 
	 * Có 2 loại throwable là error (trả về httprequest) và exception (runtimeexception và customexception) (trả về lỗi logic ở serivce)
	 */
	@ExceptionHandler(IllegalArgumentException.class) //Định nghĩa xem bắt loại lỗi nào
	@ResponseStatus(HttpStatus.BAD_REQUEST) // Quy định mã HTTP trả về - code 400
    public ErrorResponse handleBadRequest(IllegalArgumentException ex) {
        return new ErrorResponse(400, LocalDateTime.now(), ex.getMessage());
    }
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleConflict(IllegalStateException ex) {
		return new ErrorResponse(409, LocalDateTime.now(), ex.getMessage());
	}
	
	@ExceptionHandler(NoResultException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(NoResultException ex) {
		return new ErrorResponse(404, LocalDateTime.now(), ex.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleISError(Exception ex) {
		return new ErrorResponse(500, LocalDateTime.now(), ex.getMessage());
	}
}

/*Flow thêm sinh viên mới hợp lệ {"newStudentId": "SV001", "newStudentName": "Tèo"}
 * POSTMAN
 *+ Postman đóng gói dữ liệu này vào một HTTP Request và gửi qua mạng tới địa chỉ localhost cổng 8080.
 *
 *DISPATCHER SERVLET
 *+ Request đến Server (Tomcat). "Tổng quản" của Spring Boot là DispatcherServlet chặn lại
 *+ Nó nhìn địa chỉ /api/students/add_student và method POST.
 *+ Nó tra sổ địa chỉ (Handler Mapping) và hét lên: "Cái này là việc của ông StudentController, hàm addStudent!".
 *
 *CONTROLLER
 *+ Trước khi vào hàm, thư viện Jackson âm thầm làm việc. 
 *Nó lấy chuỗi JSON {"newStudentId": "SV001"...} và đổ dữ liệu vào 
 *object AddStudentRequest (DTO). Đây là tác dụng của @RequestBody.
 *+ Gọi service.add(dtoStu).
 * 
 * SERVICE
 * + Nhận DTO, kiểm tra valid đúng thì tạo Entity Student từ thông tin DTO
 * + Gọi repo.save(entity)
 * 
 * REPOSITORY + DATABASE
 * +Repository (thông qua Hibernate) dịch lệnh save thành SQL: 
 * INSERT INTO students (student_id, full_name) VALUES ('SV001', 'Tèo'); 
 * 
 * + PostgreSQL thực thi lệnh này và lưu dữ liệu vào ổ cứng
 * 
 * RESPONSE
 * + Hàm service.add chạy xong (không lỗi).
 * + Hàm controller.addStudent chạy xong.
 * + Controller trả về chuỗi "Thêm sinh viên thành công!".
 * + Spring đóng gói chuỗi này vào HTTP Response và gửi lên Postman qua Jackson với mã 200 OK .
 * + Postman nhận được và hiển thị ra màn hình.
 * 
 * -----------------------------
 * 
 * Flow báo lỗi
 * 1-2-3 giống hệ
 * SERVICE
 * + Service gọi repo.existsById("SV001").
 * + Lần này Repository trả về true (Đã có rồi!).
 * + Service hét lên (Throw Exception): throw new IllegalStateException("Mã sinh viên SV001 đã tồn tại!");
 * 
 * GLOBAL EXCEPTION HANDLER
 * + Nhưng nhờ @RestControllerAdvice, lớp GlobalExceptionHandler đứng ra "bắt" lấy cái Exception này.
 * + Nó tìm thấy hàm có gắn @ExceptionHandler(IllegalStateException.class) -> truyền lỗi vào
 * 
 * ERROR RESPONSE
 * + Tạo ra object ErrorResponse
 * + Gán status = 400
 * + Gán message = "Mã sinh viên SV001 đã tồn tại!".
 * + Gán timestamp = 27/12/2025...
 * 
 * POSTMAN 
 * + Thư viện Jackson lại xuất hiện. 
 * Nó biến object ErrorResponse thành chuỗi JSON
 * 
 * + Spring gửi JSON này về Postman kèm theo mã HTTP.
 */
