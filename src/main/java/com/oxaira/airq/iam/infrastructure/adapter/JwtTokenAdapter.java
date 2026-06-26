package com.oxaira.airq.iam.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenAdapter {

    public String issueToken(
            String email) {

        return "JWT_TOKEN";

    }
}