package com.example.learn_spring_framework.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
		Page<Student> findAllByDeletedFalse(Pageable pageable);
		
		List<Student> findAllByDeletedTrue();
		
		Optional<Student> findByStudentIdAndDeletedFalse(String studentId);
		
		Optional<Student> findStudentByStudentIdAndDeleted(String studentId, boolean deleted);
		
		//find by fullname have keyword and ignore case
		Page<Student> findByFullNameContainingIgnoreCaseAndDeletedFalse(String keyword, Pageable pageable);
		
		@Modifying
		@Transactional
	    @Query("UPDATE Student s SET s.deleted = true")
	    void softDeleteAllStudents();
}
