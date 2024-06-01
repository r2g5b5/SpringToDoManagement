package com.example.springtodomanagement.controller;

import com.example.springtodomanagement.dtos.jwt.JWTAuthResponse;
import com.example.springtodomanagement.dtos.login.AddLoginRequest;
import com.example.springtodomanagement.dtos.register.AddRegisterRequest;
import com.example.springtodomanagement.services.AuthService;
import com.example.springtodomanagement.utils.ValidationUtils;
import com.example.springtodomanagement.wrapper.Result;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService service;

    @PostMapping("/auth/register")
    public Result<String> register(@RequestBody @Valid AddRegisterRequest request, BindingResult result) {
        Result<String> validationResult = ValidationUtils.handleValidationErrors(result);
        if (validationResult != null) {
            return validationResult;
        }
        return service.register(request);
    }

    @PostMapping("/auth/login")
    public Result<JWTAuthResponse> login(@RequestBody AddLoginRequest request) {
        String token = service.login(request).data;
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        return new Result<>(jwtAuthResponse);
    }

}
