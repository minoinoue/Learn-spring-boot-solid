package com.example.learn_spring_framework.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.learn_spring_framework.model.ERole;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IRoleRepository;
import com.example.learn_spring_framework.repository.IUserRepository;


@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.config.adUserName}")
    private String adminUserName;
    
    @Value("${app.config.adPassword}")
    private String adminPassword;
    
    
    @Autowired
    public DatabaseInitializer(IRoleRepository roleRepository, IUserRepository userRepository, PasswordEncoder passwordEncoder) {
    	this.roleRepository = roleRepository;
    	this.passwordEncoder = passwordEncoder;
    	this.userRepository = userRepository;
    };

    @Override
    public void run(String... args) throws Exception {     
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
        if (roleRepository.findByName(ERole.ROLE_STUDENT).isEmpty()) {
            roleRepository.save(new Role(ERole.ROLE_STUDENT));
        }

        if (!userRepository.existsByUserName(adminUserName)) {
            User admin = new User();
            admin.setUserName(adminUserName);
            admin.setPassword(passwordEncoder.encode(adminPassword));

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("Đã tạo tài khoản admin mẫu");
        }
    }
}