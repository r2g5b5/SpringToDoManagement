package com.example.springtodomanagement.services.impl;

import com.example.springtodomanagement.dtos.login.AddLoginRequest;
import com.example.springtodomanagement.dtos.register.AddRegisterRequest;
import com.example.springtodomanagement.entities.Role;
import com.example.springtodomanagement.entities.User;
import com.example.springtodomanagement.repository.RoleRepository;
import com.example.springtodomanagement.repository.UserRepository;
import com.example.springtodomanagement.security.JWTTokenProvider;
import com.example.springtodomanagement.services.AuthService;
import com.example.springtodomanagement.wrapper.BaseResult;
import com.example.springtodomanagement.wrapper.Error;
import com.example.springtodomanagement.wrapper.ErrorCodes;
import com.example.springtodomanagement.wrapper.Result;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Override
    public Result<Long> register(AddRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return new Result<>(new Error("username: " + request.getUsername() + " exists!", ErrorCodes.ACCESS_DENIED, "username"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return new Result<>(new Error("email: " + request.getEmail() + " exists!", ErrorCodes.ACCESS_DENIED, "email"));
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = User.fromRegister(request, roles);
        user.setName(request.getName());

        User user1 = userRepository.save(user);

        return new Result<>(user1.getId());
    }

    @Override
    public Result<String> login(AddLoginRequest request) {
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsernameOrEmail(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token= jwtTokenProvider.generateToken(authentication);


        return new Result<>(token);
    }
}
