package com.example.learn_spring_framework.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.example.learn_spring_framework.dto.response.StudentItemResponse;
import com.example.learn_spring_framework.dto.response.StudentResponse;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.StudentService;

import jakarta.validation.Valid;
import java.time.LocalDateTime;

/*@RestController is used to define a controller that handles
 * RESTful web servives (return data directly like JSON)
 * 
 * Combine with @Controller and @ResponseBody
 * 
 * Note: 
 * - When one method in @RestController violates validation, it will throw
 * exception and @RestControllerAdvice catches that exception and 
 * return normalized response to client
 *
 * - When you return successful result without violate conditions,
 * JSON will take variable in DTO to show or get value through Postman's body
 * ex: http://localhost:8080/api/students/add_student
 * {"newStudentId": "SV005", "newStudentName": "Trần Nhân Doanh"}
 * 
 * It will return
 * {
    "timestamp": "29/12/2025 11:27:39",
    "status": 201,
    "message": "Thêm sinh viên mới thành công!",
    "data": {
        "studentId": "SV005",
        "fullName": "Trần Nhân Doanh"
    }
    
 *  In input body, you must write name of variable as same as you write 
 *  variable's name in DTO
 */

@RestController 
@RequestMapping("/api/students") //default URL for controller
public class StudentController {
	
	private final StudentService service;
	
	@Autowired
	public StudentController(StudentService service) {
		this.service = service;
	}

	// http://localhost:8080/api/students GET
	@GetMapping
	public ResponseEntity<StudentResponse<List<StudentItemResponse>>> showStudent() {
		List<Student> dsach = service.getAllStudent();
		/*
		 * stream() is a flexible way to process collections of object like mapping, reducing, sorting
		 * 
		 * map method each object List<Student> sang List<StudentItemResponse>
		 * 
		 * collect(Collectors.toList()) returns result stream into List
		 */
		List<StudentItemResponse> dsachList =  dsach.stream()
			.map(student -> new StudentItemResponse(student.getStudentId(), student.getFullName()))
					.collect(Collectors.toList());
		
		StudentResponse<List<StudentItemResponse>> responseBody = new StudentResponse<List<StudentItemResponse>>(LocalDateTime.now(),
																			200,
																			null,
																			dsachList);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseBody); 
	}
	
	// http://localhost:8080/api/students/1 GET
	@GetMapping("/{id}")
	//PathVariable is used to get variable from url
	public ResponseEntity<StudentResponse<Student>> findStudent(@PathVariable String id) {
			Student findStudentById = service.getById(id);
			
			StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(),
																				200,
																				"Đã tìm thấy sinh viên có " + id + " !",
																				findStudentById);
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}
	
    // http://localhost:8080/api/students/add_student POST
    @PostMapping("/add_student")
    /*
     * use @Valid to active the checking condition from DTO AddStudentRequest
     * @RequestBody need a return a body type DTO AddStudentRequest (will be transform into JSON by Jackson)
     */
	public ResponseEntity<StudentResponse<Student>> addStudent(@RequestBody @Valid AddStudentRequest dtoStu) {
    	/*ResponseEntity is represent for HTTP Response includes HTTP status code, body, HTTP headers
    	 * <StudentResponse<Student>> in here is type of return (body)
    	 * 
    	 * Context: API return HTTP response include type of DTO (StudentResponse has Student type)
    	 */
    		Student newStudent = service.add(dtoStu);
			
			StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(), 
																				201, 
																				"Thêm sinh viên mới thành công!",
																				newStudent);
			//Create a DTO return result
			return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
			//Return HTTP status code and response body DTO to client
	}
	
    //http://localhost:8080/api/students/1 PUT
    //@RequestBody need a return a body type DTO ModifyStudent so in the body you just only use newStudentName variable
    @PutMapping("/{id}")
	public ResponseEntity<StudentResponse<Student>> modifyStudent(@PathVariable String id, 
												@RequestBody @Valid ModifyStudentRequest dtoMod) {
			Student renameStudent = service.modify(id, dtoMod);
			
			StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(), 
																				200, 
																				"Cập nhật sinh viên " + id + " thành công",
																				renameStudent);
			return ResponseEntity.status(HttpStatus.OK).body(responseBody);
	}
    
    //http://localhost:8080/api/students/1 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponse<Student>> deleteStudent(@PathVariable String id) {
    		service.delete(id);
    		
    		StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(), 
    																			200,	
    																			"Đã xóa sinh viên với id = " + id + " thành công");
    		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    
    //http://localhost:8080/api/students/ DELETE
    @DeleteMapping("/delete_all")
    public ResponseEntity<StudentResponse<Student>> deleteAllStudent() {
    		service.deleteAll();
    		
    		StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(),
    																			200,
    																			"Đã xóa toàn bộ danh sách sinh viên.");
    		return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}