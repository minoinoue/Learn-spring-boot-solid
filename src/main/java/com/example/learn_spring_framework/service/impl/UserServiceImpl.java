package com.example.learn_spring_framework.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.enums.ERole;
import com.example.learn_spring_framework.model.RefreshToken;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.service.IRefreshTokenService;
import com.example.learn_spring_framework.service.IRolesService;
import com.example.learn_spring_framework.service.IUserService;
import com.example.learn_spring_framework.util.JWTUtil;

import org.springframework.transaction.annotation.Transactional;
/*
 * Connect to database to find information of real user and use for authentication
 * 
 * Purpose: To connect Spring Security to the database. It receives the username, 
 * searches the database, and converts the username from Useran Entity to UserDetails a 
 * Security Object so that Spring Security can understand it.
 */
@Service
public class UserServiceImpl implements UserDetailsService, IUserService {
	private final IUserRepository userRepo;
	private final IRolesService rolesService;
	private final IRefreshTokenService rtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtils;
	
	//@Lazy: Beans won't create AuthenticationManager until beans call it.
	@Autowired
	public UserServiceImpl(IUserRepository userRepo, @Lazy AuthenticationManager authenticationManager, JWTUtil jwtUtils, IRolesService rolesService, PasswordEncoder passwordEncoder, IRefreshTokenService rtService) {
		this.userRepo = userRepo;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.rolesService = rolesService;
		this.passwordEncoder = passwordEncoder;
		this.rtService = rtService;
	}
	
	@Override
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
        
        User user = findByUserName(userDetails.getUsername());
        RefreshToken jwtRefreshToken = rtService.createRefreshToken(user.getId());

       //transform Set<Role> into List<GrantedAuthority>
        List<String> roles = (List<String>) userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new LoginResponse(
                LocalDateTime.now(),
                jwtToken,
                jwtRefreshToken.getToken(),
                userDetails.getUsername(),
                roles
        );
    }
	
	@Override
    @Transactional
    public User createStudentUser(String userName, String password) {
    	if (userRepo.existsByUserName(userName)) {
    		throw new IllegalStateException("Username đã tồn tại!");
    	}
    	Role userRole = rolesService.getRoleByName(ERole.ROLE_STUDENT);
    	
    	String encodedPassword = passwordEncoder.encode(password);
        
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        return new User(userName, encodedPassword, roles);
    }
	
	@Override
    @Transactional
    public User modifyUser(User user, String newUserName, String newPassword) {
    	if(!user.getUserName().equals(newUserName))
    		if (userRepo.existsByUserName(newUserName)) {
                throw new IllegalStateException("Username '" + newUserName + "' đã tồn tại!");
            }
       	user.setUserName(newUserName);
       	if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới không được trùng với mật khẩu cũ!");
        }
       	String encodedPassword = passwordEncoder.encode(newPassword);
    	user.setPassword(encodedPassword);
    	
    	return userRepo.save(user);
    }
	
	@Override
	public User findByUserName(String username) {
		return userRepo.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("Tên đăng nhập hoặc mật khẩu bị sai!"));
	}
    
	@Override
	public UserDetails loadUserByUsername(String username) {
				User user = findByUserName(username);
				 
				Student student = user.getStudent();
				
				if(student != null && student.isDeleted()) {
					throw new BadCredentialsException("Tên đăng nhập hoặc mật khẩu sai!");
				}
				
				List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
		                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
		                .collect(Collectors.toList());
	
				// mapping from Entity User to UserDetails of Spring Security
		        return new org.springframework.security.core.userdetails.User( //available class in Spring Security thats implement interface UserDetails
		                user.getUserName(),           // get username in DB
		                user.getPassword(),           // get password Bcrypt in DB
		                authorities
		                /*user.getRoles() in daabase
		                 *new SimpleGrantedAuthority -> map string "STUDENT" into SimpleGrantedAuthority object -> 
		                 * 
		                 * Collections.singletonList(...) 1 users can have many roles -> List/Collection
		                 * singletonList -> list just has one value.
		                 */
		    );
		}
	}

