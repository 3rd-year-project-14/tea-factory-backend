package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.dto.UserDTO;
import com.teafactory.pureleaf.dto.InventoryManagerDto;
import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.auth.entity.Role;
import com.teafactory.pureleaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ✅ CREATE
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email is already in use: " + userDTO.getEmail());
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        UserDTO savedDTO = new UserDTO();
        BeanUtils.copyProperties(savedUser, savedDTO);
        return savedDTO;
    }

    // ✅ READ ALL
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserDTO dto = new UserDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    // ✅ READ ONE
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    // ✅ UPDATE
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        BeanUtils.copyProperties(userDTO, user, "id", "createdAt");
        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(updatedUser, dto);
        return dto;
    }

    // ✅ DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ✅ READ INVENTORY MANAGERS BY FACTORY
    public List<InventoryManagerDto> getInventoryManagersByFactoryId(Long factoryId) {
        List<User> users = userRepository.findByFactory_FactoryIdAndRole(factoryId, Role.INVENTORY_MANAGER);
        return users.stream()
                .map(user -> new InventoryManagerDto(user.getId(), user.getName()))
                .collect(Collectors.toList());
    }
}
