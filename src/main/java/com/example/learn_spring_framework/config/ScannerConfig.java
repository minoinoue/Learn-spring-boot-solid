package com.example.learn_spring_framework.config;

import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@Configuration points that class is a configuration class contains methods create Beans
 * -> It runs before the application run.
 * 
 * Purpose: Configuration libraries that can not add @Component
 */
@Configuration
public class ScannerConfig {
	/*
	 *@Bean puts above methods inside the class
	 *-> It requests Spring to set up object that returns from own method
	 *and puts it into container to manage 
	 *
	 */
	@Bean
	public Scanner scanner() {
		return new Scanner(System.in);
	}
}
