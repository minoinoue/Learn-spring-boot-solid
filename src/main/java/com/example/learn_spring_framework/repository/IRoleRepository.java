package com.example.learn_spring_framework.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learn_spring_framework.enums.ERole;
import com.example.learn_spring_framework.model.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    	Optional<Role> findByName(ERole name);
}