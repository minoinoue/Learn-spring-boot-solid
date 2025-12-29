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
	public List<Student> showStudent() {
		List<Student> dsach = service.getAllStudent();
		return dsach; 
	}
	
	// http://localhost:8080/api/students/1 GET
	@GetMapping("/{id}")
	//PathVariable is used to get variable from url
	public Student findStudent(@PathVariable String id) {
			Student findStudentById = service.getById(id);
			return findStudentById;
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
			
			StudentResponse<Student> responseBody = new StudentResponse<Student>(LocalDateTime.now(), 201, "Thêm sinh viên mới thành công!", newStudent);
			//Create a DTO return result
			return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
			//Return HTTP status code and response body DTO to client
	}
	
    //http://localhost:8080/api/students/1 PUT
    //@RequestBody need a return a body type DTO ModifyStudent so in the body you just only use newStudentName variable
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
    
    //http://localhost:8080/api/students/DELETE
    @DeleteMapping
    public ResponseEntity<String> deleteAllStudent() {
    		service.deleteAll();
    		return ResponseEntity.status(HttpStatus.OK).body("Đã xóa danh sách sinh viên thành công");
    }
}