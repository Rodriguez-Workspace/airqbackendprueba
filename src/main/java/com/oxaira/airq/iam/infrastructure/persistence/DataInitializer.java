package com.oxaira.airq.iam.infrastructure.persistence;

import com.oxaira.airq.iam.domain.model.Role;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.RoleRepository;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${spring.admin.default.email}")
    private String defaultAdminEmail;

    @org.springframework.beans.factory.annotation.Value("${spring.admin.default.password}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) {

        createRole("ADMIN");
        createRole("CLIENT");
        createRole("TECHNICIAN");

        createUser(
                "admin",
                defaultAdminEmail,
                defaultAdminPassword,
                "ADMIN"
        );



    }

    private void createRole(String roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(
                    Role.builder()
                            .name(roleName)
                            .build());
        }

    }

    private void createUser(
            String username,
            String email,
            String password,
            String roleName) {

        if (userRepository.findByEmail(email).isEmpty()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException(
                            "Role not found: " + roleName));

            userRepository.save(
                    User.builder()
                            .username(username)
                            .email(email)
                            .password(passwordEncoder.encode(password))
                            .enabled(true)
                            .createdAt(LocalDateTime.now())
                            .role(role)
                            .build());
        }

    }

}