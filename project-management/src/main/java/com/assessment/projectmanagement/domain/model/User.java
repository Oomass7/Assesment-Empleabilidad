package com.assessment.projectmanagement.domain.model;

import com.assessment.projectmanagement.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User domain model - Pure domain entity without framework dependencies
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private List<Project> projects = new ArrayList<>();
    
    @Builder.Default
    private List<Task> assignedTasks = new ArrayList<>();
    
    /**
     * Business logic: Activate user
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Business logic: Deactivate user
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Business logic: Check if user can manage projects
     */
    public boolean canManageProjects() {
        return this.role == UserRole.ADMIN || this.role == UserRole.PROJECT_MANAGER;
    }
    
    /**
     * Business logic: Check if user can be assigned tasks
     */
    public boolean canBeAssignedTasks() {
        return this.active && this.role != UserRole.GUEST;
    }
}
