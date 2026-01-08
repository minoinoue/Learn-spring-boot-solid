package com.example.learn_spring_framework.service;

import com.example.learn_spring_framework.enums.ERole;
import com.example.learn_spring_framework.model.Role;

public interface IRolesService {
	
	Role getRoleByName(ERole roleName);
	
}
