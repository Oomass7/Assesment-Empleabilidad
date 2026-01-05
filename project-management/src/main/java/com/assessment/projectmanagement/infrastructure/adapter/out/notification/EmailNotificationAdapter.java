package com.assessment.projectmanagement.infrastructure.adapter.out.notification;

import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter for sending email notifications
 * TODO: Implement actual email sending logic (e.g., using JavaMailSender)
 */
@Component
@Slf4j
public class EmailNotificationAdapter implements NotificationPort {

    @Override
    public void sendProjectCreatedNotification(Long projectId, String projectName, String ownerEmail) {
        log.info("Sending project created notification to {}: Project '{}' (ID: {}) has been created",
                ownerEmail, projectName, projectId);
        // TODO: Implement actual email sending
    }

    @Override
    public void sendTaskAssignedNotification(Long taskId, String taskTitle, String assigneeEmail) {
        log.info("Sending task assigned notification to {}: Task '{}' (ID: {}) has been assigned",
                assigneeEmail, taskTitle, taskId);
        // TODO: Implement actual email sending
    }

    @Override
    public void sendTaskCompletedNotification(Long taskId, String taskTitle, String projectOwnerEmail) {
        log.info("Sending task completed notification to {}: Task '{}' (ID: {}) has been completed",
                projectOwnerEmail, taskTitle, taskId);
        // TODO: Implement actual email sending
    }

    @Override
    public void sendProjectCompletedNotification(Long projectId, String projectName, String ownerEmail) {
        log.info("Sending project completed notification to {}: Project '{}' (ID: {}) has been completed",
                ownerEmail, projectName, projectId);
        // TODO: Implement actual email sending
    }

    @Override
    public void sendGenericNotification(String recipient, String subject, String message) {
        log.info("Sending notification to {}: Subject '{}', Message: '{}'",
                recipient, subject, message);
        // TODO: Implement actual email sending
    }
}
