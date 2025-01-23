package com.data.entity;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String role;

    // Constructor
    public UserDTO(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Getters and setters
}
