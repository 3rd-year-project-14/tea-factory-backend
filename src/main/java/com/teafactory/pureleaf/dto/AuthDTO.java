package com.teafactory.pureleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthDTO {
    private String role;
    private String username;
    private Long userId;
    private String name;
    private String email;
    private String address;
    private String nic;
    private String contactNo;
    private Long factoryId;
    private String factoryName;
    private String factoryAddress;
}
