package com.example.learn_spring_framework.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.util.JWTUtil;
/*
 * Connect to database to find information of real user and use for authentication
 * 
 * Purpose: To connect Spring Security to the database. It receives the username, 
 * searches the database, and converts the username from Useran Entity to UserDetails a 
 * Security Object so that Spring Security can understand it.
 */
@Service
public class UserService implements UserDetailsService {
	private final IUserRepository userRepo;
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtils;
	
	//@Lazy: Beans won't create AuthenticationManager until beans call it.
	@Autowired
	public UserService(IUserRepository userRepo, @Lazy AuthenticationManager authenticationManager, JWTUtil jwtUtils) {
		this.userRepo = userRepo;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}
	
    public LoginResponse login(LoginRequest loginDto) {
    	/*This method will check user and pass and throw exception when you put wrong user or password
    	 * 
    	 * Put username, password into UPAT and give to authentication Manager
    	 * after that, AM will call loadbyUserName from UserService
    	 * 
    	 * if you pass, return authentication
        */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUserName(),
                        loginDto.getPassword()
                )
        );

      //get user informations from session, type object (getPrincipal) -> mapping into UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Create token
        String jwtToken = jwtUtils.generateToken(userDetails.getUsername());

       //transform Set<Role> into List<GrantedAuthority>
        List<String> roles = (List<String>) userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new LoginResponse(
                LocalDateTime.now(),
                jwtToken,
                userDetails.getUsername(),
                roles
        );
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) {
				User user = userRepo.findByUserName(username)
				 .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản " + username));
				 
				
				List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
		                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
		                .collect(Collectors.toList());
	
				// mapping from Entity User to UserDetails of Spring Security
		        return new org.springframework.security.core.userdetails.User( //available class in Spring Security thats implement interface UserDetails
		                user.getUserName(),           // get username in DB
		                user.getPassword(),           // get password Bcrypt in DB
		                authorities
		                /*user.getRoles() in database
		                 *new SimpleGrantedAuthority -> map string "STUDENT" into SimpleGrantedAuthority object -> 
		                 * 
		                 * Collections.singletonList(...) 1 users can have many roles -> List/Collection
		                 * singletonList -> list just has one value.
		                 */
		        );
		}
	}

