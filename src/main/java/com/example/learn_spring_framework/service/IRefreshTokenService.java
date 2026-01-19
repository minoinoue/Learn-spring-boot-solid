package com.example.learn_spring_framework.service;

import java.util.Optional;

import com.example.learn_spring_framework.model.RefreshToken;

public interface IRefreshTokenService {
	Optional<RefreshToken> findByToken(String token);
	RefreshToken createRefreshToken(Long userId);
	RefreshToken verifyExpiration(RefreshToken token);
	int deleteByUserId(Long userId);
	RefreshToken getValidRefreshToken(String token);
	String createNewAccessToken(RefreshToken token);
	RefreshToken rotateRefreshToken(String oldToken);
}
