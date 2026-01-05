package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.enums.ProjectStatus;
import com.assessment.projectmanagement.domain.enums.TaskStatus;
import com.assessment.projectmanagement.domain.enums.UserRole;
import com.assessment.projectmanagement.domain.exception.BusinessException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CompleteTaskService
 * Tests the business rules for task completion
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CompleteTaskService Tests")
class CompleteTaskServiceTest {

    @Mock
    private TaskRepositoryPort taskRepository;

    @Mock
    private CurrentUserPort currentUserPort;

    @Mock
    private AuditLogPort auditLogPort;

    @Mock
    private NotificationPort notificationPort;

    private CompleteTaskService completeTaskService;

    private User owner;
    private Project project;
    private Task task;
    private Task completedTask;

    @BeforeEach
    void setUp() {
        completeTaskService = new CompleteTaskService(
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

        // Setup project
        project = Project.builder()
                .id(1L)
                .name("Test Project")
                .description("Test Description")
                .status(ProjectStatus.ACTIVE)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Setup incomplete task
        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .project(project)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Setup already completed task
        completedTask = Task.builder()
                .id(2L)
                .title("Completed Task")
                .description("Already completed")
                .status(TaskStatus.COMPLETED)
                .completedAt(LocalDateTime.now())
                .project(project)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("CompleteTask_ShouldGenerateAuditAndNotification")
    void completeTask_ShouldGenerateAuditAndNotification() {
        // Arrange
        when(currentUserPort.getCurrentUser()).thenReturn(owner);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = completeTaskService.completeTask(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isCompleted());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getCompletedAt());

        // Verify audit log was generated
        verify(auditLogPort).logUpdate(
                eq("Task"),
                eq(1L),
                eq("owner"),
                contains("Completed task: Test Task"));

        // Verify notification was sent
        verify(notificationPort).sendTaskCompletedNotification(
                eq(1L),
                eq("Test Task"),
                eq("owner@example.com"));

        // Verify task was saved
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("CompleteTask_AlreadyCompleted_ShouldFail")
    void completeTask_AlreadyCompleted_ShouldFail() {
        // Arrange
        when(currentUserPort.getCurrentUser()).thenReturn(owner);
        when(taskRepository.findById(2L)).thenReturn(Optional.of(completedTask));

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> completeTaskService.completeTask(2L));

        assertEquals("Task is already completed", exception.getMessage());

        // Verify no side effects occurred
        verify(taskRepository, never()).save(any(Task.class));
        verify(auditLogPort, never()).logUpdate(anyString(), anyLong(), anyString(), anyString());
        verify(notificationPort, never()).sendTaskCompletedNotification(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("CompleteTask_ShouldSetCompletedAt")
    void completeTask_ShouldSetCompletedAt() {
        // Arrange
        when(currentUserPort.getCurrentUser()).thenReturn(owner);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime beforeCompletion = LocalDateTime.now();

        // Act
        Task result = completeTaskService.completeTask(1L);

        LocalDateTime afterCompletion = LocalDateTime.now();

        // Assert
        assertNotNull(result.getCompletedAt());
        assertTrue(result.getCompletedAt().isAfter(beforeCompletion) ||
                result.getCompletedAt().isEqual(beforeCompletion));
        assertTrue(result.getCompletedAt().isBefore(afterCompletion) ||
                result.getCompletedAt().isEqual(afterCompletion));
    }
}
