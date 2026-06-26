package com.oxaira.airq.iam.application.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}
