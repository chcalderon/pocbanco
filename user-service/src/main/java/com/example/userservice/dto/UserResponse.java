package com.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data  // Genera getters, setters, equals, hashCode, toString
public class UserResponse {

    private String id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;
}
