package com.teafactory.pureleaf.dto;

import com.teafactory.pureleaf.entity.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String firebaseUid;
    private String email;
    private Role role;
    private String name;
    private String nic;
    private String contactNo;
    private Boolean isActive;
    private String address;
}
