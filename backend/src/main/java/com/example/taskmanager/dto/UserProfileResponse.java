package com.example.taskmanager.dto;

import com.example.taskmanager.entity.UserProfileEntity;

import java.time.Instant;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserProfileResponse from(UserProfileEntity entity) {
        return new UserProfileResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
