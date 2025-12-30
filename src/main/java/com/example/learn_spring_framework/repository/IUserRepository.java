package com.example.learn_spring_framework.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.model.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
		Optional<User> findByUserName(String userName);
		/*
		 * Because user can null so use optional to return a box has
		 * data inside.
		 * 
		 */
}
