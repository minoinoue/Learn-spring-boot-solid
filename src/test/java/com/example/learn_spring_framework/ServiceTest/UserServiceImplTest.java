package com.example.learn_spring_framework.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.enums.ERole;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.service.IRolesService;
import com.example.learn_spring_framework.service.impl.UserServiceImpl;
import com.example.learn_spring_framework.util.JWTUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	@Mock private IUserRepository userRepo;
    @Mock private IRolesService rolesService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JWTUtil jwtUtils;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;
    private Role studentRole;
	
    @BeforeEach
    void setUp() {
    	studentRole = new Role();
    	studentRole.setName(ERole.ROLE_STUDENT);
    	
    	sampleUser = new User("username", "encoded_pass", Set.of(studentRole));
    }
    
	@Test
	@DisplayName("Test find user by user name: Find user successfully")
	void findUserByUserName_shouldLoadUserSuccessfully() {
		
		when(userRepo.findByUserName("username")).thenReturn(Optional.of(sampleUser));
		
		UserDetails userDetails = userService.loadUserByUsername("username");
		
		assertThat(userDetails.getUsername()).isEqualTo("username");
		assertThat(userDetails.getAuthorities()).hasSize(1);
		assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_STUDENT");
	}
	
	@Test
	@DisplayName("Test load user by user name: Threw exception")
	void findUserByUserName_notFound_shouldThrowUsernameNotFoundException() {
		
		when(userRepo.findByUserName("notfound")).thenReturn(Optional.empty());
		
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername("notfound");
		});
	}
	
	@Test
	@DisplayName("Test create student user: Create user successfully")
	void createStudentUser_shouldSaveStudentUserSuccesfully() {
		when(userRepo.existsByUserName("newuser")).thenReturn(false);
		when(rolesService.getRoleByName(ERole.ROLE_STUDENT)).thenReturn(studentRole);
		when(passwordEncoder.encode("raw_pass")).thenReturn("encoded_pass");
		
		User createdUser = userService.createStudentUser("newuser", "raw_pass");
		
		assertThat(createdUser.getUserName()).isEqualTo("newuser");
		assertThat(createdUser.getRoles()).contains(studentRole);
		assertThat(createdUser.getPassword()).isEqualTo("encoded_pass");	
	}
	
	@Test
	@DisplayName("Test create student user: Threw IllegalStateException")
	void createStudentUser_existsUsername_shouldThrowIllegalStateException() {
		
		when(userRepo.existsByUserName("existed")).thenReturn(true);
		
		assertThrows(IllegalStateException.class, () -> {
			userService.createStudentUser("existed", "raw_pass");
		});
	}
	
	@Test
	@DisplayName("Test login: Login successfully")
	void login_shouldLoginSuccessfully() {
		// Arrange
        LoginRequest request = new LoginRequest();
        request.setPassword("password");
        request.setUserName("testuser");
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("pass")
                .authorities("ROLE_STUDENT")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken("testuser")).thenReturn("mocked_token");

        // Act
        LoginResponse response = userService.login(request);

        // Assert
        assertThat(response.getToken()).isEqualTo("mocked_token");
        assertThat(response.getUsername()).isEqualTo("testuser");
        assertThat(response.getRoles()).contains("ROLE_STUDENT");
    }
}
