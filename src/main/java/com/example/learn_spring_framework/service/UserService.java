package com.example.learn_spring_framework.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
 */
@Service
public class UserService implements UserDetailsService {
	private final IUserRepository userRepo;
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtils;
	
	@Autowired
	public UserService(IUserRepository userRepo, @Lazy AuthenticationManager authenticationManager, JWTUtil jwtUtils) {
		this.userRepo = userRepo;
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}
	
    public LoginResponse login(LoginRequest loginDto) {
    	//"authenticate" method will check user and pass and throw exception when you put wrong user or password
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

       //transform object type GrantedAuthority into List
        List<String> roles = userDetails.getAuthorities().stream()
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
				Optional<User> user = userRepo.findByUserName(username);
				if(user.isEmpty())
					throw new UsernameNotFoundException("Không thấy tài khoản " + username);
				User findUser = user.get();
				/*User user = repo.findByUserName(username)
				 * .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy sinh viên " + username));
				 */
	
				// mapping from Entity User to UserDetails of Spring Security
		        return new org.springframework.security.core.userdetails.User( //available class in Spring Security thats implement interface UserDetails
		                findUser.getUserName(),           // get username in DB
		                findUser.getPassword(),           // get password Bcrypt in DB
		                Collections.singletonList(new SimpleGrantedAuthority(findUser.getRoles())) 
		                /*user.getRoles() in database
		                 *new SimpleGrantedAuthority -> map string "STUDENT" into SimpleGrantedAuthority object -> 
		                 * 
		                 * Collections.singletonList(...) 1 users can have many roles -> List/Collection
		                 * singletonList -> list just has one value.
		                 */
		        );
		}
	}

