package com.oxaira.airq.iam.domain.service;

public class PasswordPolicyService {

    public boolean isValidPassword(
            String password) {

        return password != null
                && password.length() >= 6;

    }

}