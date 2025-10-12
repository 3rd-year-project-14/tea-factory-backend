package com.teafactory.pureleaf.auth.controller;

import com.teafactory.pureleaf.auth.dto.AuthResponse;
import com.teafactory.pureleaf.auth.dto.LoginRequest;
import com.teafactory.pureleaf.auth.dto.SignupRequest;
import com.teafactory.pureleaf.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request) throws Exception {
        authService.signup(request);
        return "Signup successful";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }

}
