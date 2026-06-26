package com.oxaira.airq.iam.application.dto;

public record TechClientResponseDTO(
        Long id,
        String name,
        String email,
        java.util.List<String> campuses
) {
}
