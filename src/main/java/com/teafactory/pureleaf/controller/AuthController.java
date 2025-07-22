package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.AuthResponse;
import com.teafactory.pureleaf.dto.LoginRequest;
import com.teafactory.pureleaf.dto.SignupRequest;
import com.teafactory.pureleaf.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

//    @PostMapping("/signup")
//    public String signup(@RequestBody SignupRequest request) throws Exception {
//        authService.signup(request);
//        return "Signup successful";
//    }
//    @PostMapping("/signup")
//    public String signup(@Valid @RequestBody SignupRequest request) throws Exception {
//        authService.signup(request);
//        return "Signup successful";
//    }
@PostMapping("/signup")
public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request, BindingResult bindingResult) throws Exception {
    if (bindingResult.hasErrors()) {
        String errors = bindingResult.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(errors);
    }
    authService.signup(request);
    return ResponseEntity.ok("Signup successful");
}


    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) throws Exception {
        return authService.login(request);
    }
}
