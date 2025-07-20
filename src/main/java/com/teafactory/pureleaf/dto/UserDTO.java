package com.teafactory.pureleaf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String firebaseUid;
    private String email;
    private String role;
    private String name;
    private String nic;
    private String contactNo;
}
