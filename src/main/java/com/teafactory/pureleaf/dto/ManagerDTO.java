package com.teafactory.pureleaf.dto;

import lombok.Data;

@Data
public class ManagerDTO {
    private String name;
    private String password;
    private String email;
    private String nic;
    private String mobile;
    private String role;
    private String factory;
}
