package com.oxaira.airq.iam.interfaces.rest;

import com.oxaira.airq.iam.application.dto.TechClientResponseDTO;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/tech/clients")
@PreAuthorize("hasRole('TECHNICIAN')")
@RequiredArgsConstructor
public class TechClientController {

    private final UserRepository userRepository;

    @GetMapping
    public List<TechClientResponseDTO> getClients() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() != null && "CLIENT".equalsIgnoreCase(user.getRole().getName()))
                .map(user -> {
                    String name = user.getUsername() != null ? user.getUsername() : "Cliente";
                    String company = user.getCompanyName() != null ? user.getCompanyName() : "Sin Empresa";
                    return new TechClientResponseDTO(
                            user.getId(),
                            name,
                            user.getEmail(),
                            company,
                            user.getCampuses() != null ? user.getCampuses() : java.util.Collections.emptyList()
                    );
                })
                .collect(Collectors.toList());
    }
}
