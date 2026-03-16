package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskResponse> findAll(TaskStatus status, String search) {
        List<TaskEntity> results;
        if (status != null && search != null) {
            results = taskRepository.findByStatusAndTitleContainingIgnoreCase(status, search);
        } else if (status != null) {
            results = taskRepository.findByStatus(status);
        } else if (search != null) {
            results = taskRepository.findByTitleContainingIgnoreCase(search);
        } else {
            results = taskRepository.findAll();
        }
        return results.stream().map(taskMapper::toResponse).toList();
    }

    public TaskResponse findById(Long id) {
        return taskMapper.toResponse(getOrThrow(id));
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
        TaskEntity entity = taskMapper.toEntity(request);
        return taskMapper.toResponse(taskRepository.save(entity));
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        TaskEntity entity = getOrThrow(id);
        taskMapper.updateEntity(entity, request);
        return taskMapper.toResponse(taskRepository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    private TaskEntity getOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
