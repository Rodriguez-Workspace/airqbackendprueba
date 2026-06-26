package com.oxaira.airq.iam.application.command;

public record RegisterUserCommand(

        String username,

        String email,

        String password

) {
}