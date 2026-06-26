package com.oxaira.airq.iam.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationAdapter {

    public boolean authenticate(
            String email,
            String password) {

        return true;

    }
}