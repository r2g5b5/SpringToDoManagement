package com.example.springtodomanagement.services;

import com.example.springtodomanagement.dtos.jwt.JWTAuthResponse;
import com.example.springtodomanagement.dtos.login.AddLoginRequest;
import com.example.springtodomanagement.dtos.register.AddRegisterRequest;
import com.example.springtodomanagement.wrapper.Result;

public interface AuthService {
    Result<Long> register(AddRegisterRequest request);
    Result<String> login(AddLoginRequest request);
}
