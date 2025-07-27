package com.teafactory.pureleaf.service;

import com.google.firebase.auth.FirebaseToken;
import com.teafactory.pureleaf.dto.AuthDTO;
import com.teafactory.pureleaf.dto.LoginRequest;
import com.teafactory.pureleaf.dto.SignupRequest;
import com.teafactory.pureleaf.entity.Role;
import com.teafactory.pureleaf.entity.User;
import com.teafactory.pureleaf.repository.UserRepository;
import com.teafactory.pureleaf.util.FirebaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private FirebaseUtil firebaseUtil;

    @Autowired
    private UserRepository userRepository;

    public void signup(SignupRequest request) throws Exception {
        FirebaseToken decodedToken = firebaseUtil.verifyToken(request.getToken());

        String email = decodedToken.getEmail();
        String firebaseUid = decodedToken.getUid();

        if (userRepository.existsByEmail(email)) {
            throw new Exception("User already exists with email: " + email);
        }

        User user = new User();
        user.setFirebaseUid(firebaseUid);
        user.setEmail(email);
        user.setName(request.getName());
        user.setNic(request.getNic());
        user.setContactNo(request.getContactNo());
        user.setAddress(request.getAddress());
        user.setRole(Role.PENDING_USER); // Signup allowed only for Supplier

        userRepository.save(user);
    }

    public AuthDTO login(LoginRequest request) throws Exception {
        FirebaseToken decodedToken = firebaseUtil.verifyToken(request.getToken());
        String email = decodedToken.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found with email: " + email));

        Long factoryId = null;
        String factoryName = null;
        String factoryAddress = null;
        if (user.getFactory() != null) {
            factoryId = user.getFactory().getFactoryId();
            factoryName = user.getFactory().getName();
            factoryAddress = user.getFactory().getAddress();
        }

        return new AuthDTO(
                user.getRole().name(),
                user.getEmail(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAddress(),
                user.getNic(),
                user.getContactNo(),
                factoryId,
                factoryName,
                factoryAddress
        );
    }
}
