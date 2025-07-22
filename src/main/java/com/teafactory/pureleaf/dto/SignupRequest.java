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
//
//package com.teafactory.pureleaf.dto;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Size;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class SignupRequest {
//
//    private String token;
//
//    @NotBlank(message = "Name is required")
//    private String name;
//
//    // NIC: either 9 digits + V/v or 12 digits
//    @NotBlank(message = "NIC is required")
//    @Pattern(
//            regexp = "^\\d{9}[vV]$|^\\d{12}$",
//            message = "NIC must be 12 digits or 9 digits followed by 'V' or 'v'"
//    )
//    private String nic;
//
//    // Contact number: exactly 10 digits
//    @NotBlank(message = "Contact number is required")
//    @Pattern(
//            regexp = "^\\d{10}$",
//            message = "Contact number must be exactly 10 digits"
//    )
//    private String contactNo;
//
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    private String email;
//
//    @NotBlank(message = "Address is required")
//    private String address;
//}
