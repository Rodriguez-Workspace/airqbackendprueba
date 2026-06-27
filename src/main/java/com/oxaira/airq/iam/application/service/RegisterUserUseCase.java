package com.oxaira.airq.iam.application.service;

import com.oxaira.airq.iam.application.dto.RegisterUserRequest;
import com.oxaira.airq.iam.domain.model.Role;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.RoleRepository;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.notifications.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public User execute(RegisterUserRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .companyName(request.companyName())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        emailService.sendTechWelcomeEmail(savedUser.getEmail(), savedUser.getUsername(), request.password());
        return savedUser;
    }
}