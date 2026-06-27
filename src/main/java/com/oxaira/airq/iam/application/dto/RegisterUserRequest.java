package com.oxaira.airq.iam.application.dto;

public record RegisterUserRequest(

        String username,

        String email,

        String password,
        
        String companyName

) {
}