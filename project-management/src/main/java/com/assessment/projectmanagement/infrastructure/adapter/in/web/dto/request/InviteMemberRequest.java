package com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InviteMemberRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
}
