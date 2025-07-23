package com.teafactory.pureleaf.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String role;
    private String name;
    private String nic;
    private String contactNo;
    private Boolean isActive;
    private String address;
    private Long factoryId;
    private FactoryDTO factory;
}
