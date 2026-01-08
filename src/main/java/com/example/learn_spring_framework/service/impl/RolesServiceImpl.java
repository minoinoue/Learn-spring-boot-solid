package com.example.learn_spring_framework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.enums.ERole;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.repository.IRoleRepository;
import com.example.learn_spring_framework.service.IRolesService;

import jakarta.persistence.NoResultException;

@Service
public class RolesServiceImpl implements IRolesService {
	private final IRoleRepository roleRepo;
	
	@Autowired
	public RolesServiceImpl(IRoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	@Override
	public Role getRoleByName(ERole roleName) {
        return roleRepo.findByName(roleName)
                .orElseThrow(() -> new NoResultException("Role " + roleName + " không tồn tại."));
    }
}
