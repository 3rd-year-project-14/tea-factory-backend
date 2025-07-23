package com.teafactory.pureleaf.service;

import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}

