package com.oxaira.airq.iam.application.service;

import com.oxaira.airq.iam.application.dto.LoginRequest;
import com.oxaira.airq.iam.application.dto.LoginResponse;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.shared.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public LoginResponse execute(
            LoginRequest request) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(
                        () -> new RuntimeException(
                                "User not found"));

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPassword());

        if (!matches) {
            throw new RuntimeException(
                    "Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().getName());

        return new LoginResponse(
                token,
                user.getRole().getName());

    }
}