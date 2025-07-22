package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String role;
    private String username;
    private Long userId;
}
