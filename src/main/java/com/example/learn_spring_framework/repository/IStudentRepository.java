package com.example.learn_spring_framework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.model.Student;

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

}
