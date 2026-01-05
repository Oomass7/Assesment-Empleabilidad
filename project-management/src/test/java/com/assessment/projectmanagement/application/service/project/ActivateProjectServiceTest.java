package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.enums.ProjectStatus;
import com.assessment.projectmanagement.domain.enums.UserRole;
import com.assessment.projectmanagement.domain.exception.BusinessException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActivateProjectService
 * Tests the business rules for project activation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ActivateProjectService Tests")
class ActivateProjectServiceTest {

    @Mock
    private ProjectRepositoryPort projectRepository;

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private CurrentUserPort currentUserPort;

    @Mock
    private AuditLogPort auditLogPort;

    @Mock
    private NotificationPort notificationPort;

    private ActivateProjectService activateProjectService;

    private User owner;
    private User nonOwner;
    private Project project;
    private Task task;

    @BeforeEach
    void setUp() {
        activateProjectService = new ActivateProjectService(
                projectRepository,
                taskRepository,
                currentUserPort,
                auditLogPort,
                notificationPort);

        // Setup owner user
        owner = User.builder()
                .id(1L)
                .username("owner")
                .email("owner@example.com")
                .password("password")
                .role(UserRole.PROJECT_MANAGER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Setup non-owner user
        nonOwner = User.builder()
                .id(2L)
                .username("nonowner")
                .email("nonowner@example.com")
                .password("password")
                .role(UserRole.DEVELOPER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Setup project
        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Test Description")
                .status(ProjectStatus.INACTIVE)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Setup task
        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .project(project)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("ActivateProject_WithTasks_ShouldSucceed")
    void activateProject_WithTasks_ShouldSucceed() {
        // Arrange
        when(currentUserPort.getCurrentUser()).thenReturn(owner);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(1L)).thenReturn(List.of(task));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = activateProjectService.activateProject(1L);

        // Assert
        assertNotNull(result);
        assertEquals(ProjectStatus.ACTIVE, result.getStatus());
        verify(projectRepository).save(any(Project.class));
        verify(auditLogPort).logUpdate(eq("Project"), eq(1L), eq("owner"), anyString());
        verify(notificationPort).sendProjectCreatedNotification(eq(1L), eq("Test Project"), eq("owner@example.com"));
    }

    @Test
    @DisplayName("ActivateProject_WithoutTasks_ShouldFail")
    void activateProject_WithoutTasks_ShouldFail() {
        // Arrange
        when(currentUserPort.getCurrentUser()).thenReturn(owner);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.findByProjectId(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> activateProjectService.activateProject(1L));

        assertEquals("Cannot activate project without tasks", exception.getMessage());
        verify(projectRepository, never()).save(any(Project.class));
        verify(auditLogPort, never()).logUpdate(anyString(), anyLong(), anyString(), anyString());
        verify(notificationPort, never()).sendProjectCreatedNotification(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("ActivateProject_ByNonOwner_ShouldFail")
    void activateProject_ByNonOwner_ShouldFail() {
        // Arrange
        when(currentUserPort.getCurrentUser()).thenReturn(nonOwner);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> activateProjectService.activateProject(1L));

        assertEquals("Only the project owner can activate it", exception.getMessage());
        verify(taskRepository, never()).findByProjectId(anyLong());
        verify(projectRepository, never()).save(any(Project.class));
        verify(auditLogPort, never()).logUpdate(anyString(), anyLong(), anyString(), anyString());
        verify(notificationPort, never()).sendProjectCreatedNotification(anyLong(), anyString(), anyString());
    }
}
