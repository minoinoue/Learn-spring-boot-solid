package com.example.learn_spring_framework.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.model.Student;

import jakarta.transaction.Transactional;

/* This @Repository is use for class that works directly with database
 * 
 * -> must extends JpaRepository
 * 
 * in JpaRepository<Student, String> 
 * -> <Student> is the Entity's name
 * -> <String> is the type of primary key
 *
 * It have many method at hand to use like save(), delete(), etc..
 */

@Repository
public interface IStudentRepository extends JpaRepository<Student, String> {
		@Query(value = "SELECT * FROM students WHERE deleted = true", nativeQuery = true)
		List<Student> findAllDeletedStudent();
		
//		@Query(value = "SELECT * FROM students WHERE student_id = :student_id AND deleted = true", nativeQuery = true)
		Optional<Student> findStudentByStudentIdAndDeleted(String studentId, boolean deleted);
		
		@Modifying
		@Transactional
	    @Query("UPDATE Student s SET s.deleted = true")
	    void softDeleteAllStudents();
}
