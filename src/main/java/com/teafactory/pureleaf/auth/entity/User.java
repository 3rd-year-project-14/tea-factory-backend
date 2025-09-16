package com.teafactory.pureleaf.auth.entity;

import com.teafactory.pureleaf.entity.Factory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firebase_uid", nullable = false)
    private String firebaseUid;


    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Optional fields (mostly for SUPPLIER role)
    @Column(name = "name")
    private String name;

    @Column(name = "nic", unique = true)
    private String nic;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "address")
    private String address;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "factory_id", referencedColumnName = "factoryId")
    private Factory factory;

}
