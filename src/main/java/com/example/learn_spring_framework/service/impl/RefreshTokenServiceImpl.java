package com.example.learn_spring_framework.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.learn_spring_framework.model.RefreshToken;
import com.example.learn_spring_framework.model.User;
import com.example.learn_spring_framework.repository.IRefreshTokenRepository;
import com.example.learn_spring_framework.repository.IUserRepository;
import com.example.learn_spring_framework.service.IRefreshTokenService;
import com.example.learn_spring_framework.util.JWTUtil;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {

	private final IRefreshTokenRepository rtRepo;
	private final JWTUtil jwtUtil;
	private final IUserRepository userRepository;
	
	@Autowired
	public RefreshTokenServiceImpl(IRefreshTokenRepository rtRepo, JWTUtil jwtUtil, IUserRepository userRepository) {
		this.jwtUtil = jwtUtil;
		this.rtRepo = rtRepo;
		this.userRepository = userRepository;
	}
	
	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return rtRepo.findByToken(token);
	}
	
	@Override
	@Transactional
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			rtRepo.delete(token);
			throw new RuntimeException("Xác thực không thành công.");
		}
		return token;
	} //checking time of refresh token
	
	@Override
	@Transactional
	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();
		
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(jwtUtil.getRefreshTokenDurationMs()));
		refreshToken.setToken(UUID.randomUUID().toString()); //random token
		
		return rtRepo.save(refreshToken);
	}
	
	@Override
	@Transactional
	public String createNewAccessToken(RefreshToken token) {
			String newAccessToken = jwtUtil.generateToken(token.getUser().getUserName());
			return newAccessToken;
	}
	
	@Override
	@Transactional
	public RefreshToken getValidRefreshToken(String token) {
		Optional<RefreshToken> refreshToken = rtRepo.findByToken(token);
		
		return verifyExpiration(refreshToken.get());
	}
	
	@Override 
	@Transactional
	public RefreshToken rotateRefreshToken(String oldRefreshTokenStr) {
		RefreshToken oldToken = getValidRefreshToken(oldRefreshTokenStr);
		
		User user = oldToken.getUser();
		
		rtRepo.delete(oldToken);
		
		return createRefreshToken(user.getId());
	}
	
	@Override
	@Transactional
    public int deleteByUserId(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isEmpty()) {
	           throw new NoResultException("Không tìm thấy user với id " + userId);
		}        
	    return rtRepo.deleteByUser(user.get());
    } //deleting refresh token for log out, output is number of deletion
}
