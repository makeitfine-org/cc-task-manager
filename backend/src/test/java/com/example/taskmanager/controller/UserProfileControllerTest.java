package com.example.taskmanager.controller;

import com.example.taskmanager.dto.UserProfileRequest;
import com.example.taskmanager.dto.UserProfileResponse;
import com.example.taskmanager.exception.GlobalExceptionHandler;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.service.UserProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProfileController.class)
@Import(GlobalExceptionHandler.class)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserProfileService service;

    private static final Instant NOW = Instant.parse("2026-03-16T10:00:00Z");

    private UserProfileResponse sampleResponse() {
        return new UserProfileResponse(1L, "jdoe", "jdoe@example.com", "John", "Doe", NOW, NOW);
    }

    @Test
    void getAll_returnsListOf200() throws Exception {
        when(service.findAll()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/user-profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("jdoe"));
    }

    @Test
    void getById_existingId_returns200() throws Exception {
        when(service.findById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/user-profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jdoe@example.com"));
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException("UserProfile", 99L));

        mockMvc.perform(get("/api/user-profiles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_validRequest_returns201() throws Exception {
        UserProfileRequest request = new UserProfileRequest("jdoe", "jdoe@example.com", "John", "Doe");
        when(service.create(any(UserProfileRequest.class))).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_invalidRequest_returns400() throws Exception {
        UserProfileRequest request = new UserProfileRequest("", "not-an-email", null, null);

        mockMvc.perform(post("/api/user-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_existingId_returns200() throws Exception {
        UserProfileRequest request = new UserProfileRequest("jdoe2", "jdoe2@example.com", "Jane", "Doe");
        UserProfileResponse updated = new UserProfileResponse(1L, "jdoe2", "jdoe2@example.com", "Jane", "Doe", NOW, NOW);
        when(service.update(eq(1L), any(UserProfileRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/user-profiles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jdoe2"));
    }

    @Test
    void delete_existingId_returns204() throws Exception {
        mockMvc.perform(delete("/api/user-profiles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_unknownId_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("UserProfile", 99L)).when(service).delete(99L);

        mockMvc.perform(delete("/api/user-profiles/99"))
                .andExpect(status().isNotFound());
    }
}
