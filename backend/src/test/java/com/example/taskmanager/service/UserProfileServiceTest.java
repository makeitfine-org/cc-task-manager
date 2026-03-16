package com.example.taskmanager.service;

import com.example.taskmanager.dto.UserProfileRequest;
import com.example.taskmanager.dto.UserProfileResponse;
import com.example.taskmanager.entity.UserProfileEntity;
import com.example.taskmanager.exception.DuplicateResourceException;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository repository;

    @InjectMocks
    private UserProfileService service;

    private static final Instant NOW = Instant.parse("2026-03-16T10:00:00Z");

    private UserProfileEntity sampleEntity() {
        UserProfileEntity e = new UserProfileEntity();
        e.setId(1L);
        e.setUsername("jdoe");
        e.setEmail("jdoe@example.com");
        e.setFirstName("John");
        e.setLastName("Doe");
        e.setCreatedAt(NOW);
        e.setUpdatedAt(NOW);
        return e;
    }

    @Test
    void findAll_returnsAllProfiles() {
        when(repository.findAll()).thenReturn(List.of(sampleEntity()));

        List<UserProfileResponse> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).username()).isEqualTo("jdoe");
    }

    @Test
    void findById_existingId_returnsProfile() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleEntity()));

        UserProfileResponse result = service.findById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("jdoe@example.com");
    }

    @Test
    void findById_unknownId_throwsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_uniqueCredentials_savesAndReturnsProfile() {
        UserProfileRequest request = new UserProfileRequest("jdoe", "jdoe@example.com", "John", "Doe");
        when(repository.existsByUsername("jdoe")).thenReturn(false);
        when(repository.existsByEmail("jdoe@example.com")).thenReturn(false);
        when(repository.save(any(UserProfileEntity.class))).thenReturn(sampleEntity());

        UserProfileResponse result = service.create(request);

        assertThat(result.username()).isEqualTo("jdoe");
        verify(repository).save(any(UserProfileEntity.class));
    }

    @Test
    void create_duplicateUsername_throwsDuplicateException() {
        UserProfileRequest request = new UserProfileRequest("jdoe", "other@example.com", null, null);
        when(repository.existsByUsername("jdoe")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("jdoe");

        verify(repository, never()).save(any());
    }

    @Test
    void create_duplicateEmail_throwsDuplicateException() {
        UserProfileRequest request = new UserProfileRequest("newuser", "jdoe@example.com", null, null);
        when(repository.existsByUsername("newuser")).thenReturn(false);
        when(repository.existsByEmail("jdoe@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("jdoe@example.com");
    }

    @Test
    void update_existingId_updatesAndReturnsProfile() {
        UserProfileEntity existing = sampleEntity();
        UserProfileRequest request = new UserProfileRequest("jdoe2", "jdoe2@example.com", "Jane", "Doe");
        UserProfileEntity updated = sampleEntity();
        updated.setUsername("jdoe2");
        updated.setEmail("jdoe2@example.com");
        updated.setFirstName("Jane");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByUsername("jdoe2")).thenReturn(false);
        when(repository.existsByEmail("jdoe2@example.com")).thenReturn(false);
        when(repository.save(existing)).thenReturn(updated);

        UserProfileResponse result = service.update(1L, request);

        assertThat(result.username()).isEqualTo("jdoe2");
    }

    @Test
    void update_unknownId_throwsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new UserProfileRequest("x", "x@x.com", null, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existingId_deletesProfile() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_unknownId_throwsNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).deleteById(any());
    }
}
