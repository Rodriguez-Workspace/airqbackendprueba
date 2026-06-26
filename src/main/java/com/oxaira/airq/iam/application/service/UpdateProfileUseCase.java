package com.oxaira.airq.iam.application.service;

import com.oxaira.airq.iam.application.dto.UpdateProfileRequest;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateProfileUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User execute(String currentEmail, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.email() != null && !request.email().isEmpty() && !request.email().equals(user.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.email());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.email());
        }

        if (request.username() != null && !request.username().isEmpty()) {
            user.setUsername(request.username());
        }

        if (request.password() != null && !request.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.campuses() != null) {
            java.util.List<String> validCampuses = request.campuses().stream()
                    .filter(c -> c != null && !c.trim().isEmpty())
                    .map(String::trim)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
            user.setCampuses(validCampuses);
        }

        return userRepository.save(user);
    }
}
