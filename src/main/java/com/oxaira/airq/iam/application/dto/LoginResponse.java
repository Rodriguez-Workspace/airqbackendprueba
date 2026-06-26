package com.oxaira.airq.iam.application.dto;

public record LoginResponse(

        String token,
        String role

) {
}