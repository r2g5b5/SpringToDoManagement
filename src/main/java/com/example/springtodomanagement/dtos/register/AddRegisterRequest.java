package com.example.springtodomanagement.dtos.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRegisterRequest {
    private String name;
    private String username;
    private String email;
    private String password;
}
