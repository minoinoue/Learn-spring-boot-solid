package com.example.learn_spring_framework.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.dto.response.StudentInfoResponse;
import com.example.learn_spring_framework.mapper.IStudentMapper;
import com.example.learn_spring_framework.dto.response.ApiResponse;
import com.example.learn_spring_framework.dto.response.PageResponse;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.IStudentService;

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
	
	private final IStudentService stuService;
	private final IStudentMapper stuMapper;
	
	@Autowired
	public StudentController(IStudentService stuService, IStudentMapper stuMapper) {
		this.stuService = stuService;
		this.stuMapper = stuMapper;
	}

	// http://localhost:8080/api/students GET
	//GET /api/students?sortBy=studentId&sortDir=desc -> desc sort by studentID 
	//GET /api/students?sortBy=studentId&sortDir=asc -> asc sort by studentID 
	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<StudentInfoResponse>>> showStudent(
			@RequestParam(defaultValue = "1") int page,  // default page 1
            @RequestParam(defaultValue = "10") int size,  // default 10 items in 1 page
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir //default ascending
			) {
		
		int currentPage = (page < 1) ? 1 : page;
		
		Page<Student> dsach = stuService.getAllStudent(currentPage, size, sortBy, sortDir);
			/*
			 * stream() is a flexible way to process collections of object like mapping, reducing, sorting
			 * 
			 * map method each object in List<Student> sang List<StudentItemResponse>
			 * 
			 * collect(Collectors.toList()) returns result stream into List
			 */
			List<StudentInfoResponse> dsachList = stuMapper.toResponseList(dsach.getContent());
			
			PageResponse<StudentInfoResponse> pageResponse = new PageResponse<>(
	                currentPage,
	                size,
	                dsach.getTotalElements(),
	                dsach.getTotalPages(),
	                dsachList
	        );
			
			ApiResponse<PageResponse<StudentInfoResponse>> responseBody = new ApiResponse<PageResponse<StudentInfoResponse>>
																						(LocalDateTime.now(),
																						200,
																						"Lấy danh sách thành công",
																						pageResponse);
			
			return ResponseEntity.ok(responseBody); 
	}
	
	// http://localhost:8080/api/students/bin GET
    @GetMapping("/bin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StudentInfoResponse>>> getAllDeletedStudent() {
    		List<Student> deletedList = stuService.getBin();
    		
   
    		List<StudentInfoResponse> responseList = stuMapper.toResponseList(deletedList);
    		
    		ApiResponse<List<StudentInfoResponse>> responseBody = new ApiResponse<>(
											    	            LocalDateTime.now(),
											    	            200,
											    	            "Danh sách sinh viên đã xóa",
											    	            responseList);
    		
    		return ResponseEntity.ok(responseBody);
  
    }
	
	// http://localhost:8080/api/students/1 GET
	@GetMapping("/{id}")
	//PathVariable is used to get variable from url
	public ResponseEntity<ApiResponse<StudentInfoResponse>> findStudent(@PathVariable String id) {
		Student student = stuService.getById(id);
		
		StudentInfoResponse responseData = stuMapper.toResponse(student);
		
		ApiResponse<StudentInfoResponse> responseBody = new ApiResponse<>(
				                                              LocalDateTime.now(), 
				                                              200, 
				                                              "Đã tìm thấy sinh viên có " + id + " !", 
				                                              responseData);
		return ResponseEntity.ok(responseBody);
}
	
    // http://localhost:8080/api/students/add_student POST
    @PostMapping("/add_student")
    /*
     * use @Valid to active the checking condition from DTO AddStudentRequest
     * @RequestBody need a return a body type DTO AddStudentRequest (will be transform into JSON by Jackson)
     */
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<StudentInfoResponse>> addStudent(@RequestBody @Valid AddStudentRequest dtoStu) {
    	/*ResponseEntity is represent for HTTP Response includes HTTP status code, body, HTTP headers
    	 * <StudentResponse<Student>> in here is type of return (body)
    	 * 
    	 * Context: API return HTTP response include type of DTO (StudentResponse has Student type)
    	 */
    		Student newStudent = stuService.add(dtoStu);
    		
    		StudentInfoResponse responseData = stuMapper.toResponse(newStudent);
			
			ApiResponse<StudentInfoResponse> responseBody = new ApiResponse<StudentInfoResponse>
															(LocalDateTime.now(), 
															201, 
															"Thêm sinh viên mới thành công!",
															responseData);
			//Create a DTO return result
			return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
			//Return HTTP status code and response body DTO to client
	}
	
    //http://localhost:8080/api/students/1 PUT
    //@RequestBody need a return a body type DTO ModifyStudent so in the body you just only use newStudentName variable
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentInfoResponse>> modifyStudent(@PathVariable String id, 
			@RequestBody @Valid ModifyStudentRequest dtoMod) {
			Student reStudent = stuService.modify(id, dtoMod);
			
			StudentInfoResponse responseData = stuMapper.toResponse(reStudent);
			
			ApiResponse<StudentInfoResponse> responseBody = new ApiResponse<>(
																LocalDateTime.now(), 
																200, 
																"Cập nhật sinh viên " + id + " thành công", 
																responseData);
			return ResponseEntity.ok(responseBody);
}
    
    //http://localhost:8080/api/students/1 DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String id) {
    	stuService.delete(id);
		
		ApiResponse<Void> responseBody = new ApiResponse<>(
				                                          LocalDateTime.now(), 
				                                          200, 
				                                          "Đã xóa sinh viên với id = " + id + " thành công");
		return ResponseEntity.ok(responseBody);
}
    
    //http://localhost:8080/api/students/ DELETE
    @DeleteMapping("/delete_all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAllStudent() {
    	stuService.deleteAll();
		
		ApiResponse<Void> responseBody = new ApiResponse<>(
				                                          LocalDateTime.now(), 
				                                          200, 
				                                          "Đã xóa toàn bộ danh sách sinh viên.");
		return ResponseEntity.ok(responseBody);
}
    
 // http://localhost:8080/api/students/restore/SV001 PATCH
    @PatchMapping("/restore/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentInfoResponse>> restoreStudent(@PathVariable String id) {
    	stuService.restore(id);
        Student restoredStudent = stuService.getById(id);

		StudentInfoResponse responseData = stuMapper.toResponse(restoredStudent);

        ApiResponse<StudentInfoResponse> responseBody = new ApiResponse<>(
        		                                            LocalDateTime.now(), 
        		                                            200, 
        		                                            "Khôi phục sinh viên " + id + " thành công!", 
        		                                            responseData);
        return ResponseEntity.ok(responseBody);
}
}