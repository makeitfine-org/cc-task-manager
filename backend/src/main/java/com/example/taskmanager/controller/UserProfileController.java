package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserProfileRequest;
import com.example.taskmanager.dto.UserProfileResponse;
import com.example.taskmanager.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserProfileResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserProfileResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse create(@Valid @RequestBody UserProfileRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public UserProfileResponse update(@PathVariable Long id,
                                      @Valid @RequestBody UserProfileRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
