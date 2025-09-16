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
}
