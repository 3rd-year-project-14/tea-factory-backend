package com.teafactory.pureleaf.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerInfo {
    private Long id;
    private String email;
    private String name;
    private String role;
    private Long factoryId;
}

