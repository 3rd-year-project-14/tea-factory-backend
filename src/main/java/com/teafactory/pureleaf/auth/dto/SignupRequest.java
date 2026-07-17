package com.teafactory.pureleaf.auth.dto;

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

//“This is a DTO used to receive signup requests from the frontend.
//It contains user info like token, name, NIC, contact number, email, and address.
//Lombok @Getter and @Setter automatically create getter and setter methods.”