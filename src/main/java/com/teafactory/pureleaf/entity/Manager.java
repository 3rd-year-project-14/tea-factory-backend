package com.teafactory.pureleaf.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password; // consider hashing
    private String email;
    private String nic;
    private String mobile;
    private String role;
    private String factory;

    public void setName(String name) {
    }

    public void setPassword(String password) {
    }

    public void setEmail(String email) {
    }

    public void setNic(String nic) {
    }

    public void setMobile(String mobile) {
    }

    public void setRole(String role) {
    }

    public void setFactory(String factory) {
    }
}
