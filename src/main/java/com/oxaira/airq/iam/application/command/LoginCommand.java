package com.oxaira.airq.iam.application.command;

public record LoginCommand(

        String email,

        String password

) {
}