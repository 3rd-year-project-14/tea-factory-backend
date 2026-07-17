package com.teafactory.pureleaf.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String token;
}
//It contains a single field token sent from the frontend.
