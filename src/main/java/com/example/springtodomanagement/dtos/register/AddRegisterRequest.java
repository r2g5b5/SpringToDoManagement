package com.example.springtodomanagement.dtos.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRegisterRequest {

    @Size(min = 2, max = 100, message = "name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "username is mandatory")
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "email is mandatory")
    @Schema(example = "example@example.com")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "email should be valid")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Size(min = 4, max = 50, message = "password must be between 4 and 50 characters")
    private String password;
}
