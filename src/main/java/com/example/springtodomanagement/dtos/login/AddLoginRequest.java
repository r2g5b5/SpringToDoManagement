package com.example.springtodomanagement.dtos.login;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddLoginRequest {
    private String usernameOrEmail;
    private String password;
}
