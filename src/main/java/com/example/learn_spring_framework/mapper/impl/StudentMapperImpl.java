package com.example.learn_spring_framework.mapper.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.learn_spring_framework.dto.response.StudentInfoResponse;
import com.example.learn_spring_framework.mapper.IStudentMapper;
import com.example.learn_spring_framework.model.Student;

@Component
public class StudentMapperImpl implements IStudentMapper{
	
	@Override
    public StudentInfoResponse toResponse(Student student) {
        if (student == null) return null;
        return new StudentInfoResponse(student.getStudentId(), student.getFullName());
    }

    @Override
    public List<StudentInfoResponse> toResponseList(List<Student> students) {
        return students.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
