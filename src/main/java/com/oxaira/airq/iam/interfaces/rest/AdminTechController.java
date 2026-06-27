package com.oxaira.airq.iam.interfaces.rest;

import com.oxaira.airq.iam.application.dto.CreateTechRequest;
import com.oxaira.airq.iam.application.dto.TechResponse;
import com.oxaira.airq.iam.domain.model.Role;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.RoleRepository;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.notifications.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/v1/admin/users/tech")
@RequiredArgsConstructor
public class AdminTechController {

    private static final String TEMP_PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%&*";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TechResponse> getTechnicians() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && "TECHNICIAN".equalsIgnoreCase(user.getRole().getName()))
                .map(user -> new TechResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getZone(),
                        user.getEnabled() != null && user.getEnabled() ? "ACTIVE" : "PENDING_VERIFICATION",
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User createTechnician(@RequestBody CreateTechRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName("TECHNICIAN")
                .orElseThrow(() -> new RuntimeException("Role not found: TECHNICIAN"));

        String tempPassword = generateTemporaryPassword();

        User user = User.builder()
                .username(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(tempPassword))
                .phone(request.phone())
                .zone(request.zone())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        try {
            emailService.sendTechWelcomeEmail(savedUser.getEmail(), savedUser.getUsername(), tempPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error enviando email a: " + savedUser.getEmail() + " - " + e.getMessage());
            // The user is already created; the email can be retried separately if needed.
        }

        return savedUser;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTechnician(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 12)
                .mapToObj(i -> String.valueOf(TEMP_PASSWORD_CHARS.charAt(random.nextInt(TEMP_PASSWORD_CHARS.length()))))
                .collect(Collectors.joining());
    }
}
