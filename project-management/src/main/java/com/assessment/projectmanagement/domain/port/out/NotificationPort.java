package com.assessment.projectmanagement.domain.port.out;

/**
 * Output port for sending notifications
 */
public interface NotificationPort {

    void sendProjectCreatedNotification(Long projectId, String projectName, String ownerEmail);

    void sendTaskAssignedNotification(Long taskId, String taskTitle, String assigneeEmail);

    void sendTaskCompletedNotification(Long taskId, String taskTitle, String projectOwnerEmail);

    void sendProjectCompletedNotification(Long projectId, String projectName, String ownerEmail);

    void sendGenericNotification(String recipient, String subject, String message);
}
