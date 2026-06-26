package com.oxaira.airq.iam.application.dto;

import java.time.LocalDateTime;

public record TechResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String zone,
        String status,
        LocalDateTime createdAt
) {
}
