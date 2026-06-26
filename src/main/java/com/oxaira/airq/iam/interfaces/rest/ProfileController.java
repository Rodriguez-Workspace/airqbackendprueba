package com.oxaira.airq.iam.interfaces.rest;

import com.oxaira.airq.iam.application.dto.UpdateProfileRequest;
import com.oxaira.airq.iam.application.service.UpdateProfileUseCase;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UpdateProfileUseCase updateProfileUseCase;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<User> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request) {
        
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User updatedUser = updateProfileUseCase.execute(email, request);
        return ResponseEntity.ok(updatedUser);
    }
}
