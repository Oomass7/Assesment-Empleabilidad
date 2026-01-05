package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.enums.TaskStatus;
import com.assessment.projectmanagement.domain.exception.BusinessException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteTaskServiceTest {

        @Mock
        private TaskRepositoryPort taskRepository;
        @Mock
        private CurrentUserPort currentUserPort;
        @Mock
        private AuditLogPort auditLogPort;
        @Mock
        private NotificationPort notificationPort;

        @InjectMocks
        private CompleteTaskService completeTaskService;

        @Test
        @DisplayName("CompleteTask_AlreadyCompleted_ShouldFail")
        void shouldFailWhenTaskIsAlreadyCompleted() {
                // Arrange
                Long taskId = 1L;
                Long userId = 100L;
                User owner = User.builder().id(userId).build();
                Project project = Project.builder().owner(owner).build();

                Task completedTask = Task.builder()
                                .id(taskId)
                                .project(project)
                                .status(TaskStatus.COMPLETED)
                                .completedAt(LocalDateTime.now())
                                .build();

                when(currentUserPort.getCurrentUser()).thenReturn(owner);
                when(taskRepository.findById(taskId)).thenReturn(Optional.of(completedTask));

                // Act & Assert
                assertThrows(BusinessException.class, () -> completeTaskService.completeTask(taskId));
                verify(taskRepository, never()).save(any());
        }

        @Test
        @DisplayName("CompleteTask_ShouldGenerateAuditAndNotification")
        void shouldSucceedAndGenerateAuditAndNotification() {
                // Arrange
                Long taskId = 1L;
                Long userId = 100L;
                User owner = User.builder().id(userId).username("testuser").email("test@example.com").build();
                Project project = Project.builder().owner(owner).build();

                Task pendingTask = Task.builder()
                                .id(taskId)
                                .title("Test Task")
                                .project(project)
                                .status(TaskStatus.IN_PROGRESS)
                                .build();

                when(currentUserPort.getCurrentUser()).thenReturn(owner);
                when(taskRepository.findById(taskId)).thenReturn(Optional.of(pendingTask));
                when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                Task result = completeTaskService.completeTask(taskId);

                // Assert
                assertEquals(TaskStatus.COMPLETED, result.getStatus());
                assertNotNull(result.getCompletedAt());

                verify(auditLogPort).logUpdate(eq("Task"), eq(taskId), eq("testuser"), contains("Completed task"));
                verify(notificationPort).sendTaskCompletedNotification(eq(taskId), eq("Test Task"),
                                eq("test@example.com"));
        }

        @Test
        @DisplayName("CompleteTask_ByNonOwner_ShouldFail")
        void shouldFailWhenUserIsNotProjectOwner() {
                // Arrange
                Long taskId = 1L;
                Long ownerId = 100L;
                Long otherUserId = 999L;

                User owner = User.builder().id(ownerId).build();
                User otherUser = User.builder().id(otherUserId).build();
                Project project = Project.builder().owner(owner).build();

                Task task = Task.builder().id(taskId).project(project).build();

                when(currentUserPort.getCurrentUser()).thenReturn(otherUser);
                when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

                // Act & Assert
                assertThrows(UnauthorizedException.class, () -> completeTaskService.completeTask(taskId));
                verify(taskRepository, never()).save(any());
        }
}
