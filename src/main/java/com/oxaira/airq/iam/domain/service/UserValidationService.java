package com.oxaira.airq.iam.domain.service;

public class UserValidationService {

    public boolean isValidEmail(
            String email) {

        return email != null
                && email.contains("@");

    }

}