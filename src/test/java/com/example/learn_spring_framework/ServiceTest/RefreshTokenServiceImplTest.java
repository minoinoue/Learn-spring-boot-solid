package com.example.learn_spring_framework.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.learn_spring_framework.model.RefreshToken;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IRefreshTokenRepository;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.service.impl.RefreshTokenServiceImpl;
import com.example.learn_spring_framework.util.JWTUtil;

import jakarta.persistence.NoResultException;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTest {

    @Mock private IRefreshTokenRepository rtRepo;
    @Mock private JWTUtil jwtUtil;
    @Mock private IUserRepository userRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private User sampleUser;
    private RefreshToken sampleToken;
    private String tokenStr;

    @BeforeEach
    void setUp() {
        tokenStr = UUID.randomUUID().toString();
        
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUserName("testuser");

        sampleToken = new RefreshToken();
        sampleToken.setToken(tokenStr);
        sampleToken.setUser(sampleUser);
        sampleToken.setExpiryDate(Instant.now().plusMillis(60000));
    }


    @Test
    @DisplayName("verify expriration successfuly")
    void verifyExpiration_shouldSuccessfully() {
        RefreshToken result = refreshTokenService.verifyExpiration(sampleToken);
        assertThat(result).isEqualTo(sampleToken);
        verify(rtRepo, never()).delete(any());
    }

    @Test
    @DisplayName("verify expriration error")
    void verifyExpiration_Expired_shouldThrowException() {
        sampleToken.setExpiryDate(Instant.now().minusMillis(1000));

        RuntimeException ex = assertThrows(RuntimeException.class, 
            () -> refreshTokenService.verifyExpiration(sampleToken));
        
        assertThat(ex.getMessage()).isEqualTo("Xác thực không thành công.");
        verify(rtRepo, times(1)).delete(sampleToken);
    }

    @Test
    @DisplayName("Create refresh token successfully")
    void createRefreshToken_shouldSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(jwtUtil.getRefreshTokenDurationMs()).thenReturn(60000L);
        when(rtRepo.save(any(RefreshToken.class))).thenReturn(sampleToken);

        RefreshToken result = refreshTokenService.createRefreshToken(1L);

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(sampleUser);
        verify(rtRepo, times(1)).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Create access token successfully")
    void createNewAccessToken_shouldSuccessfully() {
        when(jwtUtil.generateToken("testuser")).thenReturn("new_access_token");

        String result = refreshTokenService.createNewAccessToken(sampleToken);

        assertThat(result).isEqualTo("new_access_token");
        verify(jwtUtil, times(1)).generateToken("testuser");
    }
    
    @Test
    @DisplayName("get valid token successfully")
    void getValidRefreshToken_shouldSuccess() {
        when(rtRepo.findByToken(tokenStr)).thenReturn(Optional.of(sampleToken));

        RefreshToken result = refreshTokenService.getValidRefreshToken(tokenStr);

        assertThat(result).isEqualTo(sampleToken);
    }

    @Test
    @DisplayName("not found refresh token")
    void getValidRefreshToken_NotFound_shouldThrowException() {
        when(rtRepo.findByToken("invalid")).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, 
            () -> refreshTokenService.getValidRefreshToken("invalid"));
    }

    @Test
    @DisplayName("rotate refresh token successfully")
    void rotateRefreshToken_shouldSuccess() {
        
        when(rtRepo.findByToken(tokenStr)).thenReturn(Optional.of(sampleToken));
       
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(rtRepo.save(any(RefreshToken.class))).thenReturn(new RefreshToken());

        RefreshToken result = refreshTokenService.rotateRefreshToken(tokenStr);

        assertThat(result).isNotNull();
        verify(rtRepo, times(1)).delete(sampleToken);
        verify(rtRepo, times(1)).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("Deleted token by user id successfully")
    void deleteByUserId_shouldSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(rtRepo.deleteByUser(sampleUser)).thenReturn(1);

        int deletedCount = refreshTokenService.deleteByUserId(1L);

        assertThat(deletedCount).isEqualTo(1);
        verify(rtRepo, times(1)).deleteByUser(sampleUser);
    }

    @Test
    @DisplayName("deleted token by user id return error")
    void deleteByUserId_userNotFound_shouldThrowNoResultException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoResultException.class, 
            () -> refreshTokenService.deleteByUserId(99L));
    }
}