package com.example.springtodomanagement.services.impl;

import com.example.springtodomanagement.config.JwtService;
import com.example.springtodomanagement.dtos.login.AddLoginRequest;
import com.example.springtodomanagement.dtos.register.AddRegisterRequest;
import com.example.springtodomanagement.entities.Role;
import com.example.springtodomanagement.entities.User;
import com.example.springtodomanagement.repository.RoleRepository;
import com.example.springtodomanagement.repository.UserRepository;
import com.example.springtodomanagement.wrapper.ErrorCodes;
import com.example.springtodomanagement.wrapper.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private AddRegisterRequest registerRequest;
    private AddLoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = AddRegisterRequest.builder()
                .name("hazhir")
                .username("hazhir49")
                .email("test@test.com")
                .password("Introduce local variable").build();

        loginRequest = AddLoginRequest.builder()
                .usernameOrEmail("hazhir49")
                .password("h1234@").build();

        user = User.builder()
                .name("hazhir")
                .username("hazhir49")
                .email("test@test.com")
                .password("encodedPassword")
                .roles(new HashSet<>()).build();
    }

    @Test
    void register_UserAlreadyExistsByUsername() {
        // Arrange
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        // Act
        Result<String> result = authService.register(registerRequest);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().getFirst().getErrorCode()).isEqualTo(ErrorCodes.ACCESS_DENIED);
    }

    @Test
    void register_UserAlreadyExistsByEmail() {
        // Arrange
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // Act
        Result<String> result = authService.register(registerRequest);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().getFirst().getErrorCode()).isEqualTo(ErrorCodes.ACCESS_DENIED);
    }

    @Test
    void register_SuccessfulRegistration() {
        // Arrange
        Role userRole = new Role(1L, "ROLE_USER");

        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(jwtTokenProvider.generateToken(any(User.class))).thenReturn("jwtToken");

        // Act
        Result<String> result = authService.register(registerRequest);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo(registerRequest.getName());
        assertThat(savedUser.getUsername()).isEqualTo(registerRequest.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRoles()).extracting(Role::getName).containsExactlyInAnyOrder("ROLE_USER");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo("jwtToken");
    }

    @Test
    void login_SuccessfulLogin() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn("jwtToken");

        // Act
        Result<String> result = authService.login(loginRequest);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo("jwtToken");
    }

    @Test
    void login_Failure() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}
