package com.example.learn_spring_framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.learn_spring_framework.model.Student;

public interface StudentRepository extends JpaRepository<Student, String> {

}
