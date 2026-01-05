package com.assessment.projectmanagement.domain.model;

import com.assessment.projectmanagement.domain.enums.ProjectStatus;
import com.assessment.projectmanagement.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project domain model - Pure domain entity without framework dependencies
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User owner;

    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    /**
     * Business logic: Activate project
     */
    public void activate() {
        if (this.status == ProjectStatus.COMPLETED) {
            throw new BusinessException("Cannot activate a completed project");
        }
        this.status = ProjectStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Complete project
     */
    public void complete() {
        if (this.status == ProjectStatus.INACTIVE) {
            throw new BusinessException("Cannot complete an inactive project");
        }

        // Check if all tasks are completed
        boolean hasIncompleteTasks = tasks.stream()
                .anyMatch(task -> !task.isCompleted());

        if (hasIncompleteTasks) {
            throw new BusinessException("Cannot complete project with incomplete tasks");
        }

        this.status = ProjectStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Suspend project
     */
    public void suspend() {
        if (this.status == ProjectStatus.COMPLETED) {
            throw new BusinessException("Cannot suspend a completed project");
        }
        this.status = ProjectStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Add task to project
     */
    public void addTask(Task task) {
        if (this.status == ProjectStatus.COMPLETED) {
            throw new BusinessException("Cannot add tasks to a completed project");
        }
        this.tasks.add(task);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business logic: Check if project is overdue
     */
    public boolean isOverdue() {
        return this.endDate != null &&
                LocalDateTime.now().isAfter(this.endDate) &&
                this.status != ProjectStatus.COMPLETED;
    }

    /**
     * Business logic: Calculate completion percentage
     */
    public double getCompletionPercentage() {
        if (tasks.isEmpty()) {
            return 0.0;
        }

        long completedTasks = tasks.stream()
                .filter(Task::isCompleted)
                .count();

        return (completedTasks * 100.0) / tasks.size();
    }
}
