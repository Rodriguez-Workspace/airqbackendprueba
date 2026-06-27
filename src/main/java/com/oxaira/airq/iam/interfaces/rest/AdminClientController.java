package com.oxaira.airq.iam.interfaces.rest;

import com.oxaira.airq.iam.application.dto.ActivateClientRequest;
import com.oxaira.airq.iam.application.dto.AdminClientResponse;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.subscription.domain.model.Subscription;
import com.oxaira.airq.subscription.infrastructure.persistence.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/clients")
@RequiredArgsConstructor
public class AdminClientController {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminClientResponse> getClients() {
        return userRepository.findAll().stream()
                .filter(user -> "CLIENT".equalsIgnoreCase(user.getRole().getName()))
                .map(user -> {
                    Subscription sub = user.getSubscription();
                    
                    String institutionName = user.getCompanyName() != null ? user.getCompanyName() : "Sin Asignar";
                    String planName = "N/A";
                    Integer sensorsCount = 0;
                    Integer sensorsLimit = 0;
                    Double billing = 0.0;
                    
                    if (sub != null) {
                        if (sub.getOrganizationName() != null) {
                            institutionName = sub.getOrganizationName();
                        }
                        planName = sub.getPlan().getName();
                        sensorsCount = sub.getAssignedSensorsCount() != null ? sub.getAssignedSensorsCount() : 0;
                        sensorsLimit = sub.getPlan().getBaseSensorLimit();
                        
                        // Calculate billing
                        double basePrice = sub.getPlan().getBasePrice();
                        double extraPrice = sub.getPlan().getExtraSensorPrice();
                        long extraSensors = 0;
                        if (sensorsCount > sensorsLimit) {
                            extraSensors = sensorsCount - sensorsLimit;
                        }
                        billing = basePrice + (extraSensors * extraPrice);
                    }

                    // Map status to what the frontend expects
                    String status = user.getEnabled() != null && user.getEnabled() ? "ACTIVE" : "PENDING_VERIFICATION";

                    return new AdminClientResponse(
                            user.getId(),
                            institutionName,
                            user.getUsername(), // Using username as contact name
                            user.getEmail(),
                            planName,
                            sensorsCount,
                            sensorsLimit,
                            billing,
                            status
                    );
                })
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateClient(
            @PathVariable Long id,
            @RequestBody ActivateClientRequest request) {
            
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        
        // Handle Subscription
        Subscription subscription = subscriptionRepository.findByUserId(id).orElse(null);
        if (subscription == null) {
            subscription = Subscription.builder()
                    .user(user)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusYears(1))
                    .status("ACTIVE")
                    .build();
        }
        
        subscription.setOrganizationName(request.organizationName() != null ? request.organizationName() : user.getUsername() + " Org");
        subscription.setPlan(request.plan());
        subscription.setAssignedSensorsCount(request.initialSensorsCount() != null ? request.initialSensorsCount() : 0);
        
        user.setSubscription(subscription); // Important to update the relationship
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> suspendClient(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false); // Disable access
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}