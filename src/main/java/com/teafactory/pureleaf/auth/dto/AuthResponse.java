package com.teafactory.pureleaf.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String address;
    private String contactNo;
    private String email;
    private String name;
    private String role;
    private Long factoryId;
    private String factoryName;
    private Long userId;
}
