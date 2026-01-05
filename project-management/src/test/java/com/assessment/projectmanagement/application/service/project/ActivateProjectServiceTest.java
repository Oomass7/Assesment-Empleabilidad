package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.enums.ProjectStatus;
import com.assessment.projectmanagement.domain.enums.TaskStatus;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

        @InjectMocks
        private ActivateProjectService activateProjectService;

        @Test
        @DisplayName("ActivateProject_WithTasks_ShouldSucceed")
        void shouldSucceedWhenProjectHasActiveTasksAndUserIsOwner() {
                // Arrange
                Long projectId = 1L;
                Long userId = 100L;
                User owner = User.builder().id(userId).username("owner").email("owner@test.com").build();

                Project project = Project.builder()
                                .id(projectId)
                                .name("Test Project")
                                .owner(owner)
                                .status(ProjectStatus.INACTIVE) // Estado inicial
                                .build();

                Task activeTask = Task.builder()
                                .id(1L)
                                .status(TaskStatus.TODO) // Tarea activa
                                .project(project)
                                .build();

                when(currentUserPort.getCurrentUser()).thenReturn(owner);
                when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
                when(taskRepository.findByProjectId(projectId)).thenReturn(List.of(activeTask));
                when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                Project result = activateProjectService.activateProject(projectId);

                // Assert
                assertEquals(ProjectStatus.ACTIVE, result.getStatus());
                verify(projectRepository).save(project);
                verify(auditLogPort).logUpdate(eq("Project"), eq(projectId), eq("owner"), anyString());
                verify(notificationPort).sendProjectCreatedNotification(eq(projectId), eq("Test Project"),
                                eq("owner@test.com"));
        }

        @Test
        @DisplayName("ActivateProject_WithoutTasks_ShouldFail")
        void shouldFailWhenProjectHasNoTasks() {
                // Arrange
                Long projectId = 1L;
                Long userId = 100L;
                User owner = User.builder().id(userId).build();
                Project project = Project.builder().id(projectId).owner(owner).build();

                when(currentUserPort.getCurrentUser()).thenReturn(owner);
                when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
                when(taskRepository.findByProjectId(projectId)).thenReturn(Collections.emptyList());

                // Act & Assert
                assertThrows(BusinessException.class, () -> activateProjectService.activateProject(projectId));
                verify(projectRepository, never()).save(any());
        }

        @Test
        @DisplayName("ActivateProject_WithOnlyCompletedTasks_ShouldFail")
        void shouldFailWhenProjectHasOnlyCompletedTasks() {
                // Arrange
                Long projectId = 1L;
                Long userId = 100L;
                User owner = User.builder().id(userId).build();
                Project project = Project.builder().id(projectId).owner(owner).build();

                Task completedTask = Task.builder().status(TaskStatus.COMPLETED).build();

                when(currentUserPort.getCurrentUser()).thenReturn(owner);
                when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
                when(taskRepository.findByProjectId(projectId)).thenReturn(List.of(completedTask));

                // Act & Assert
                assertThrows(BusinessException.class, () -> activateProjectService.activateProject(projectId));
                verify(projectRepository, never()).save(any());
        }

        @Test
        @DisplayName("ActivateProject_ByNonOwner_ShouldFail")
        void shouldFailWhenUserIsNotOwner() {
                // Arrange
                Long projectId = 1L;
                Long ownerId = 100L;
                Long otherUserId = 999L;

                User owner = User.builder().id(ownerId).build();
                User otherUser = User.builder().id(otherUserId).build();

                Project project = Project.builder().id(projectId).owner(owner).build();

                when(currentUserPort.getCurrentUser()).thenReturn(otherUser);
                when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

                // Act & Assert
                assertThrows(UnauthorizedException.class, () -> activateProjectService.activateProject(projectId));
                verify(projectRepository, never()).save(any());
        }
}
