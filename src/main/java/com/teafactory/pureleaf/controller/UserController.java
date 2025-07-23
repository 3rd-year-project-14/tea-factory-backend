package com.teafactory.pureleaf.controller;

import com.teafactory.pureleaf.dto.FactoryDTO;
import com.teafactory.pureleaf.dto.UserDTO;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole().name());
                    dto.setName(user.getName());
                    dto.setNic(user.getNic());
                    dto.setContactNo(user.getContactNo());
                    dto.setIsActive(user.getIsActive());
                    dto.setAddress(user.getAddress());
                    if (user.getFactory() != null) {
                        FactoryDTO factoryDTO = new FactoryDTO();
                        factoryDTO.setId(user.getFactory().getFactoryId());
                        factoryDTO.setName(user.getFactory().getName());
                        factoryDTO.setLocation(user.getFactory().getAddress());
                        factoryDTO.setImage(user.getFactory().getImage());
                        dto.setFactory(factoryDTO);
                    }
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
