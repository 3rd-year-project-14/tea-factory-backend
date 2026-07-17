package com.teafactory.pureleaf.repository;

import com.teafactory.pureleaf.auth.entity.User;
import com.teafactory.pureleaf.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByFactory_FactoryIdAndRole(Long factoryId, Role role);
    Optional<User> findByFirebaseUid(String firebaseUid);
}
//This is a Spring Data JPA repository for the User entity.
//It extends JpaRepository to get basic CRUD operations.
//The custom methods are used to find users by email, check if an email exists, get users by factory and role, and find a user using the Firebase UID.”

//“We use an interface for the repository because we don’t need to write the implementation.
//Spring automatically provides the implementation at runtime.”