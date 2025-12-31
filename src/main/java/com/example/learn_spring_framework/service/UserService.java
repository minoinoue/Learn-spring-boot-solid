package com.example.learn_spring_framework.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.dto.request.AddStudentRequest;
import com.example.learn_spring_framework.model.Student;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IStudentRepository;
import com.example.learn_spring_framework.repository.IUserRepository;

@Service
public class UserService implements UserDetailsService {
	private final IUserRepository userRepo;
	private final IStudentRepository stuRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(IUserRepository userRepo, IStudentRepository stuRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.stuRepo = stuRepo;
		this.passwordEncoder = passwordEncoder;
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

