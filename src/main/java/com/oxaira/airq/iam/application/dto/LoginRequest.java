package com.oxaira.airq.iam.application.dto;

public record LoginRequest(

        String email,

        String password

) {
}