package com.example.learn_spring_framework;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.learn_spring_framework.controller.StudentController;

@SpringBootApplication
public class LearnSpringFrameworkApplication {

	@Autowired
    private StudentController control;
	
	public static void main(String[] args) {
		
		SpringApplication.run(LearnSpringFrameworkApplication.class, args);

	}
}    
