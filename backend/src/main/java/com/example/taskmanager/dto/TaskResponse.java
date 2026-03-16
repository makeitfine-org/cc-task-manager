package com.example.taskmanager.dto;

import com.example.taskmanager.entity.TaskPriority;
import com.example.taskmanager.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
