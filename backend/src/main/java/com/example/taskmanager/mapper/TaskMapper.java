package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(TaskRequest request) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setStatus(request.status());
        entity.setPriority(request.priority());
        return entity;
    }

    public TaskResponse toResponse(TaskEntity entity) {
        return new TaskResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public void updateEntity(TaskEntity entity, TaskRequest request) {
        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setStatus(request.status());
        entity.setPriority(request.priority());
    }
}
