package com.example.learn_spring_framework.mapper;

import com.example.learn_spring_framework.dto.response.TokenRefreshResponse;

public interface IAuthMapper {
	TokenRefreshResponse toRefreshResponse(String refreshToken, String newAccessToken);
}
