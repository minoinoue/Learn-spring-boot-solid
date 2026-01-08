package com.example.learn_spring_framework.mapper;

import java.util.List;

import com.example.learn_spring_framework.dto.response.StudentInfoResponse;
import com.example.learn_spring_framework.model.Student;

public interface IStudentMapper {
	
	//mapping 1 object
	StudentInfoResponse toResponse(Student student);
	
	//mapping a list
	List<StudentInfoResponse> toResponseList(List<Student> students);

}
