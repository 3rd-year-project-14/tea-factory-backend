package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.UserDTO;
import com.teafactory.pureleaf.entity.Role;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:*")
@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "*") // ✅ allow frontend access
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        // Duplicate email check
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use.");
        }

        // User object create
        User user = new User();
        user.setFirebaseUid(userDTO.getFirebaseUid());
        user.setEmail(userDTO.getEmail());

        // Enum Role mapping
        try {
            user.setRole(Role.valueOf(userDTO.getRole().toUpperCase().replace(" ", "_")));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid role provided.");
        }

        user.setName(userDTO.getName());
        user.setNic(userDTO.getNic());
        user.setContactNo(userDTO.getContactNo());

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }
}
