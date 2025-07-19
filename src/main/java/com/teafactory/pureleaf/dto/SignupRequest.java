package com.teafactory.pureleaf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String token;
    private String name;
    private String nic;
    private String contactNo;
    private String email;
    private String address;

}
