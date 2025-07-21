package com.teafactory.pureleaf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String token;
    private String username;
    private String password;
}
