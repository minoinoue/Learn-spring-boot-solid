package com.example.learn_spring_framework.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;

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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.learn_spring_framework.dto.request.LoginRequest;
import com.example.learn_spring_framework.dto.response.LoginResponse;
import com.example.learn_spring_framework.enums.ERole;
import com.example.learn_spring_framework.model.RefreshToken;
import com.example.learn_spring_framework.model.Role;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.service.IRefreshTokenService;
import com.example.learn_spring_framework.service.IRolesService;
import com.example.learn_spring_framework.service.impl.UserServiceImpl;
import com.example.learn_spring_framework.util.JWTUtil;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	@Mock private IUserRepository userRepo;
    @Mock private IRolesService rolesService;
    @Mock private IRefreshTokenService rtService;
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
    	sampleUser.setId(1L);
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
		
		UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () ->
			userService.loadUserByUsername("notfound"));
		
		assertThat(ex.getMessage()).isEqualTo("Tên đăng nhập hoặc mật khẩu bị sai!");
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
        request.setUserName("username");
        Authentication auth = mock(Authentication.class);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("username")
                .password("encoded_pass")
                .authorities("ROLE_STUDENT")
                .build();
        
        RefreshToken mockRefreshToken = new RefreshToken();
        mockRefreshToken.setToken("refresh_token_string");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken("username")).thenReturn("jwt_access_token");
        when(userRepo.findByUserName("username")).thenReturn(Optional.of(sampleUser));
        when(rtService.createRefreshToken(anyLong())).thenReturn(mockRefreshToken);
  

        // Act
        LoginResponse response = userService.login(request);

        // Assert
        assertThat(response.getToken()).isEqualTo("jwt_access_token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh_token_string");
        assertThat(response.getUsername()).isEqualTo("username");
        assertThat(response.getRoles()).contains("ROLE_STUDENT");
    }
	
	@Test
    @DisplayName("modify user successfullt")
    void modifyUser_shouldModifySuccessfully() {
        when(userRepo.existsByUserName("new_name")).thenReturn(false);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(passwordEncoder.encode("new_pass")).thenReturn("new_encoded_pass");
        when(userRepo.save(any(User.class))).thenReturn(sampleUser);

        User modified = userService.modifyUser(sampleUser, "new_name", "new_pass");

        assertThat(modified.getUserName()).isEqualTo("new_name");
        assertThat(modified.getPassword()).isEqualTo("new_encoded_pass");
    }
	
	@Test
    @DisplayName("Modify user: error same old password")
    void modifyUser_SamePassword_ShouldThrowException() {
        when(passwordEncoder.matches("old_pass", sampleUser.getPassword())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, 
            () -> userService.modifyUser(sampleUser, "username", "old_pass"));
    }

    @Test
    @DisplayName("Modify user: not found user")
    void findByUserName_NotFound_ShouldThrowBadCredentials() {
        when(userRepo.findByUserName("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUserName("unknown"));
    }
}
