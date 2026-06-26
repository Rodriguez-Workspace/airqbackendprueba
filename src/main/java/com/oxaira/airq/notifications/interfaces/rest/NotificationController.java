package com.oxaira.airq.notifications.interfaces.rest;

import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.notifications.application.dto.NotificationEntityDTO;
import com.oxaira.airq.notifications.domain.model.NotificationEntity;
import com.oxaira.airq.notifications.infrastructure.persistence.NotificationEntityRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/client/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationEntityRepository notificationRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<NotificationEntityDTO>> getClientNotifications(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        User client = userRepository.findByEmail(authentication.getName()).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        List<NotificationEntity> notifications = notificationRepository.findByClientOrderByCreatedAtDesc(client);
        List<NotificationEntityDTO> dtos = notifications.stream()
                .map(NotificationEntityDTO::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        User client = userRepository.findByEmail(authentication.getName()).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        NotificationEntity notification = notificationRepository.findById(id).orElse(null);
        if (notification == null || !notification.getClient().getId().equals(client.getId())) {
            return ResponseEntity.notFound().build();
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok().build();
    }
}