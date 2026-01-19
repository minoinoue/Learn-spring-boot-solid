package com.example.learn_spring_framework.controllerTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.learn_spring_framework.controller.StudentController;
import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.ModifyStudentRequest;
import com.example.learn_spring_framework.dto.response.StudentInfoResponse;
import com.example.learn_spring_framework.mapper.IStudentMapper;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.service.IStudentService;
import com.example.learn_spring_framework.service.IUserService;
import com.example.learn_spring_framework.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.NoResultException;

@WebMvcTest(StudentController.class) 
//ignore @Componenent, service and repository beans
//apply on configuration that relates on web (@Controller, ControllerAdvice, JsonComponent...) 
@AutoConfigureMockMvc(addFilters = false) // ignore Security filters to test logic method
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    //to fake http request

    @Autowired
    private ObjectMapper objectMapper;

    // mock service and mapper
	@MockBean private IStudentService stuService;
    @MockBean private IStudentMapper stuMapper;
    
    @MockBean private IUserService userService;
    @MockBean private JWTUtil jwtUtils;

    private Student mockStudent;
    private StudentInfoResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockStudent = new Student("SV001", "Nguyen Van A", null);
        mockResponse = new StudentInfoResponse("SV001", "Nguyen Van A");
        /*Using mock test
         * 
         * -> Test the controller to see if the json and status is being returned correctly.
         * can run a thousand of tests in second and focus on logic of your method.
         */
    }

    @Test
    @DisplayName("GET /api/students is successfull")
    void showStudent_shouldSuccessful() throws Exception {
        Page<Student> studentPage = new PageImpl<>(List.of(mockStudent));
        when(stuService.getAllStudent(anyInt(), anyInt(), anyString(), anyString())).thenReturn(studentPage);
        when(stuMapper.toResponseList(anyList())).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/students")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("GET /search is successful")
    void showContainingStudent_shouldSuccess() throws Exception {
        Page<Student> searchPage = new PageImpl<>(List.of(mockStudent));
        when(stuService.getAllContainingStudent(anyInt(), anyInt(), anyString(), anyString(), anyString())).thenReturn(searchPage);
        when(stuMapper.toResponseList(anyList())).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/students/search")
                .param("keyword", "Nguyen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lấy danh sách thành công"));
    }

    @Test
    @DisplayName("GET /bin is successful")
    void getAllDeletedStudent_shouldSuccess() throws Exception {
        when(stuService.getBin()).thenReturn(List.of(mockStudent));
        when(stuMapper.toResponseList(anyList())).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/students/bin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Danh sách sinh viên đã xóa"));
    }

    @Test
    @DisplayName("GET /{id} is successful")
    void findStudent_shouldSuccessful() throws Exception {
        when(stuService.getById("SV001")).thenReturn(mockStudent);
        when(stuMapper.toResponse(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/students/SV001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.studentId").value("SV001"));
    }

    @Test
    @DisplayName("GET /{id} if error return 404 Not Found")
    void findStudent_Error_shouldReturnNotResultException() throws Exception {
        when(stuService.getById("SV999")).thenThrow(new NoResultException("Không tìm thấy sinh viên SV999"));

        mockMvc.perform(get("/api/students/SV999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("POST /add_student is successful")
    void addStudent_shouldSuccessful() throws Exception {
        AddStudentRequest request = new AddStudentRequest();
        request.setNewStudentId("SV002");
        request.setNewStudentName("Nguyen Van B");
        request.setUserName("user02");
        request.setPassword("pass123");
        when(stuService.add(any())).thenReturn(mockStudent);
        when(stuMapper.toResponse(any())).thenReturn(new StudentInfoResponse("SV002", "Nguyen Van B"));

        mockMvc.perform(post("/api/students/add_student")
                .contentType(MediaType.APPLICATION_JSON) //type json
                .content(objectMapper.writeValueAsString(request))) //create json object from java object then return JSON as String type  
                .andExpect(status().isCreated()) //check status
                .andExpect(jsonPath("$.status").value(201)); //check element in json response
    }

    @Test
    @DisplayName("POST /add_student if error should return validation 400")
    void addStudent_Error_shouldReturnValidation() throws Exception {
        AddStudentRequest invalidRequest = new AddStudentRequest(); 

        mockMvc.perform(post("/api/students/add_student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /{id} is successful")
    void modifyStudent_shouldSuccessful() throws Exception {
        ModifyStudentRequest request = new ModifyStudentRequest();
        request.setNewStudentName("Updated Name");
        request.setNewUserName("newuser");
        request.setNewPassword("newpass");
        when(stuService.modify(anyString(), any())).thenReturn(mockStudent);
        when(stuMapper.toResponse(any())).thenReturn(new StudentInfoResponse("SV001", "Updated Name"));

        mockMvc.perform(put("/api/students/SV001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Cập nhật sinh viên SV001 thành công"));
    }

    @Test
    @DisplayName("DELETE /{id} is successful")
    void deleteStudent_shouldSuccessful() throws Exception {
        mockMvc.perform(delete("/api/students/SV001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("DELETE /delete_all is successfull")
    void deleteAllStudent_shouldSuccessful() throws Exception {
        mockMvc.perform(delete("/api/students/delete_all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Đã xóa toàn bộ danh sách sinh viên."));
    }

    @Test
    @DisplayName("PATCH /restore/{id} is successful")
    void restoreStudent_shouldSuccess() throws Exception {
        when(stuService.getById("SV001")).thenReturn(mockStudent);
        when(stuMapper.toResponse(any())).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/students/restore/SV001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Khôi phục sinh viên SV001 thành công!"));
    }
}