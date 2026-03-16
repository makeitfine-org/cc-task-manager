package com.example.taskmanager.service;

import com.example.taskmanager.dto.UserProfileRequest;
import com.example.taskmanager.dto.UserProfileResponse;
import com.example.taskmanager.entity.UserProfileEntity;
import com.example.taskmanager.exception.DuplicateResourceException;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public List<UserProfileResponse> findAll() {
        return repository.findAll().stream()
                .map(UserProfileResponse::from)
                .toList();
    }

    public UserProfileResponse findById(Long id) {
        return UserProfileResponse.from(getOrThrow(id));
    }

    @Transactional
    public UserProfileResponse create(UserProfileRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already taken: " + request.username());
        }
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        UserProfileEntity entity = UserProfileEntity.builder()
                .username(request.username())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        return UserProfileResponse.from(repository.save(entity));
    }

    @Transactional
    public UserProfileResponse update(Long id, UserProfileRequest request) {
        UserProfileEntity entity = getOrThrow(id);

        if (!entity.getUsername().equals(request.username())
                && repository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already taken: " + request.username());
        }
        if (!entity.getEmail().equals(request.email())
                && repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already registered: " + request.email());
        }

        entity.setUsername(request.username());
        entity.setEmail(request.email());
        entity.setFirstName(request.firstName());
        entity.setLastName(request.lastName());

        return UserProfileResponse.from(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("UserProfile", id);
        }
        repository.deleteById(id);
    }

    private UserProfileEntity getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserProfile", id));
    }
}
