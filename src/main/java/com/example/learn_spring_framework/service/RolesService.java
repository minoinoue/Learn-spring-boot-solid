package com.example.learn_spring_framework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.model.ERole;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.repository.IRoleRepository;

import jakarta.persistence.NoResultException;

@Service
public class RolesService {
	private final IRoleRepository roleRepo;
	
	@Autowired
	public RolesService(IRoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	public Role getRoleByName(ERole roleName) {
        return roleRepo.findByName(roleName)
                .orElseThrow(() -> new NoResultException("Role " + roleName + " không tồn tại."));
    }
}
