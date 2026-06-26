package com.oxaira.airq.iam.application.dto;

public record UpdateProfileRequest(
        String username,
        String email,
        String password,
        java.util.List<String> campuses
) {}
