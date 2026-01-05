package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.User;

/**
 * Output port for getting current authenticated user information
 */
public interface CurrentUserPort {

    User getCurrentUser();

    String getCurrentUsername();

    Long getCurrentUserId();

    boolean isAuthenticated();
}
