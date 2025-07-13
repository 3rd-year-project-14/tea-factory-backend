//package com.teafactory.pureleaf.service;
//
//import com.google.firebase.auth.FirebaseToken;
//import com.teafactory.pureleaf.dto.AuthResponse;
//import com.teafactory.pureleaf.dto.LoginRequest;
//import com.teafactory.pureleaf.dto.SignupRequest;
//import com.teafactory.pureleaf.entity.Role;
//import com.teafactory.pureleaf.entity.User;
//import com.teafactory.pureleaf.repository.UserRepository;
//import com.teafactory.pureleaf.util.FirebaseUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthService {
//
//    @Autowired
//    private FirebaseUtil firebaseUtil;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public void signup(SignupRequest request) throws Exception {
//        FirebaseToken decodedToken = firebaseUtil.verifyToken(request.getToken());
//        String email = decodedToken.getEmail();
//
//        if (userRepository.existsByEmail(email)) {
//            throw new Exception("User already exists with email: " + email);
//        }
//
////        User user = new User();
////        user.setEmail(email);
////        user.setRole(Role.SUPPLIER);  // Signup is only for suppliers
////        user.setName(request.getName());
////        user.setNic(request.getNic());
////        user.setContactNo(request.getContactNo());
////
////
////        userRepository.save(user);
//
//
//        User user = new User();
//        String firebaseUid = decodedToken.getUid(); // ✅
//        // ✅ Important
//        user.setEmail(email);
//        String name = request.getName();
//        user.setNic(request.getNic());
//
//        user.setContactNo(request.getContactNo());
//        user.setRole(Role.SUPPLIER); // or other role
//
//        userRepository.save(user); // ✅ will not throw 500 now
//
//    }
//
//    public AuthResponse login(LoginRequest request) throws Exception {
//        FirebaseToken decodedToken = firebaseUtil.verifyToken(request.getToken());
//        String email = decodedToken.getEmail();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new Exception("User not found with email: " + email));
//
//        return new AuthResponse(user.getRole().name());
//    }
//}
package com.teafactory.pureleaf.service;

import com.google.firebase.auth.FirebaseToken;
import com.teafactory.pureleaf.dto.AuthResponse;
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
        // ✅ 1. Verify the Firebase token
        FirebaseToken decodedToken = firebaseUtil.verifyToken(request.getToken());

        String email = decodedToken.getEmail();
        String firebaseUid = decodedToken.getUid(); // ✅ Get UID from Firebase token

        // ✅ 2. Check if the user already exists
        if (userRepository.existsByEmail(email)) {
            throw new Exception("User already exists with email: " + email);
        }

        // ✅ 3. Create and populate User entity
        User user = new User();
        user.setFirebaseUid(firebaseUid); // ✅ MUST be set to avoid null errors
        user.setEmail(email);
        user.setName(request.getName());
        user.setNic(request.getNic());
        user.setContactNo(request.getContactNo());
        user.setRole(Role.SUPPLIER); // ✅ Signup is only allowed for SUPPLIER

        // ✅ 4. Save to DB
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) throws Exception {
        FirebaseToken decodedToken = firebaseUtil.verifyToken(request.getToken());
        String email = decodedToken.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found with email: " + email));

        return new AuthResponse(user.getRole().name());
    }
}
