package com.assessment.projectmanagement.domain.model;

import com.assessment.projectmanagement.domain.enums.TaskPriority;
import com.assessment.projectmanagement.domain.enums.TaskStatus;
import com.assessment.projectmanagement.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Task domain model - Pure domain entity without framework dependencies
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Project project;
    private User assignedTo;

    /**
     * Business logic: Start task
     */
    public void start() {
        if (this.status == TaskStatus.COMPLETED) {
            throw new BusinessException("Cannot start a completed task");
        }
        if (this.assignedTo == null) {
            throw new BusinessException("Cannot start a task without an assigned user");
        }
        this.status = TaskStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Complete task
     */
    public void complete() {
        if (this.status == TaskStatus.COMPLETED) {
            throw new BusinessException("Task is already completed");
        }
        this.status = TaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Assign task to user
     */
    public void assignTo(User user) {
        if (this.status == TaskStatus.COMPLETED) {
            throw new BusinessException("Cannot reassign a completed task");
        }
        if (!user.canBeAssignedTasks()) {
            throw new BusinessException("User cannot be assigned tasks");
        }
        this.assignedTo = user;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Check if task is completed
     */
    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    /**
     * Business logic: Check if task is overdue
     */
    public boolean isOverdue() {
        return this.dueDate != null &&
                LocalDateTime.now().isAfter(this.dueDate) &&
                !isCompleted();
    }

    /**
     * Business logic: Update priority
     */
    public void updatePriority(TaskPriority newPriority) {
        if (this.status == TaskStatus.COMPLETED) {
            throw new BusinessException("Cannot update priority of a completed task");
        }
        this.priority = newPriority;
        this.updatedAt = LocalDateTime.now();
    }
}
