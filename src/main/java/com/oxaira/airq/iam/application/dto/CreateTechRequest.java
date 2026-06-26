package com.oxaira.airq.iam.application.dto;

public record CreateTechRequest(
        String fullName,
        String email,
        String phone,
        String zone
) {
}
