package com.teafactory.pureleaf.auth.entity;

public enum Role {
    PENDING_USER,
    PENDING_SUPPLIER,
    SUPPLIER,
    DRIVER,
    FACTORY_MANAGER,
    INVENTORY_MANAGER,
    FERTILIZER_MANAGER,
    ESTATE_MANAGER,
    TRANSPORT_MANAGER,
    OWNER;
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
//This enum defines all user roles in the system.
//It helps identify different user types such as Supplier, Driver, and Managers.
//The getAuthority() method adds the ROLE_ prefix so the role can be used by Spring Security for authorization.”