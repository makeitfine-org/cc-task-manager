package com.example.taskmanager.repository;

import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByStatus(TaskStatus status);

    List<TaskEntity> findByTitleContainingIgnoreCase(String title);

    List<TaskEntity> findByStatusAndTitleContainingIgnoreCase(TaskStatus status, String title);
}
