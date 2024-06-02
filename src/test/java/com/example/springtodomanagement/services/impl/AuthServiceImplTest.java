package com.example.springtodomanagement.services.impl;

import com.example.springtodomanagement.config.JwtService;
import com.example.springtodomanagement.dtos.login.AddLoginRequest;
import com.example.springtodomanagement.dtos.register.AddRegisterRequest;
import com.example.springtodomanagement.entities.Role;
import com.example.springtodomanagement.entities.User;
import com.example.springtodomanagement.repository.RoleRepository;
import com.example.springtodomanagement.repository.UserRepository;
import com.example.springtodomanagement.wrapper.Error;
import com.example.springtodomanagement.wrapper.ErrorCodes;
import com.example.springtodomanagement.wrapper.Result;
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
import java.util.Set;

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

    @Test
    void register_UserAlreadyExistsByUsername() {
        // Arrange
        AddRegisterRequest request = new AddRegisterRequest("hazhir", "hazhir49", "test@test.com", "h1234@");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // Act
        Result<String> result = authService.register(request);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSize(1);
        Error error = result.getErrors().getFirst();
        assertThat(error.getMessage()).isEqualTo("username: " + request.getUsername() + " exists!");
        assertThat(error.getErrorCode()).isEqualTo(ErrorCodes.ACCESS_DENIED);
    }

    @Test
    void register_UserAlreadyExistsByEmail() {
        // Arrange
        AddRegisterRequest request = new AddRegisterRequest("hazhir", "hazhir49", "test@test.com", "h1234@");
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act
        Result<String> result = authService.register(request);

        // Assert
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getErrors()).hasSize(1);
        Error error = result.getErrors().getFirst();
        assertThat(error.getMessage()).isEqualTo("email: " + request.getEmail() + " exists!");
        assertThat(error.getErrorCode()).isEqualTo(ErrorCodes.ACCESS_DENIED);
    }

    @Test
    void register_SuccessfulRegistration() {
        // Arrange
        AddRegisterRequest request = new AddRegisterRequest("hazhir", "hazhir49", "test@test.com", "h1234@");
        Role userRole = new Role(1L, "ROLE_USER");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtTokenProvider.generateToken(any(User.class))).thenReturn("jwtToken");

        // Act
        Result<String> result = authService.register(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo(request.getName());
        assertThat(savedUser.getUsername()).isEqualTo(request.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRoles()).extracting(Role::getName).containsExactlyInAnyOrder("ROLE_USER");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo("jwtToken");
    }

    @Test
    void login_SuccessfulLogin() {
        // Arrange
        AddLoginRequest request = new AddLoginRequest("hazhir49", "h1234@");
        User user = new User(1L, "hazhir", "hazhir49", "test@test.com", "encodedPassword", new HashSet<>());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(user)).thenReturn("jwtToken");

        // Act
        Result<String> result = authService.login(request);

        // Assert
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getData()).isEqualTo("jwtToken");
    }

    @Test
    void login_Failure() {
        // Arrange
        AddLoginRequest request = new AddLoginRequest("hazhir49", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}
