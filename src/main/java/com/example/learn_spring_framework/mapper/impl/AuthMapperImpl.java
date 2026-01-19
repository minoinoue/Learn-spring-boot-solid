package com.example.learn_spring_framework.mapper.impl;

import org.springframework.stereotype.Component;

import com.example.learn_spring_framework.dto.response.TokenRefreshResponse;
import com.example.learn_spring_framework.mapper.IAuthMapper;

@Component
public class AuthMapperImpl implements IAuthMapper{

    @Override
    public TokenRefreshResponse toRefreshResponse(String refreshToken, String newAccessToken) {
        return new TokenRefreshResponse(newAccessToken, refreshToken);
    }
}
