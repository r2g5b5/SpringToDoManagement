package com.example.springtodomanagement.services.impl;

import com.example.springtodomanagement.config.JwtService;
import com.example.springtodomanagement.dtos.login.AddLoginRequest;
import com.example.springtodomanagement.dtos.register.AddRegisterRequest;
import com.example.springtodomanagement.entities.Role;
import com.example.springtodomanagement.entities.User;
import com.example.springtodomanagement.repository.RoleRepository;
import com.example.springtodomanagement.repository.UserRepository;
import com.example.springtodomanagement.services.AuthService;
import com.example.springtodomanagement.wrapper.Error;
import com.example.springtodomanagement.wrapper.ErrorCodes;
import com.example.springtodomanagement.wrapper.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public Result<String> register(AddRegisterRequest request) {

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

        userRepository.save(user);

        String jwtToken = jwtTokenProvider.generateToken(user);

        return new Result<>(jwtToken);
    }

    @Override
    public Result<String> login(AddLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail()).orElseThrow();
        String jwtToken = jwtTokenProvider.generateToken(user);

        return new Result<>(jwtToken);
    }
}
