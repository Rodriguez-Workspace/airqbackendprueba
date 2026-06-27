package com.oxaira.airq.iam.application.dto;

public record TechClientResponseDTO(
        Long id,
        String name,
        String email,
        String companyName,
        java.util.List<String> campuses
) {
}
