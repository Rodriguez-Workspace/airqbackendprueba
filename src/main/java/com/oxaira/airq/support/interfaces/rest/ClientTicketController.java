package com.oxaira.airq.support.interfaces.rest;

import com.oxaira.airq.support.application.command.CreateTicketCommand;
import com.oxaira.airq.support.application.dto.ClientTicketRequestDTO;
import com.oxaira.airq.support.application.dto.TicketResponseDTO;
import com.oxaira.airq.support.application.service.TicketCommandService;
import com.oxaira.airq.support.application.service.TicketQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/client/tickets")
@PreAuthorize("hasRole('CLIENT') or hasRole('USER')")
public class ClientTicketController {

    private final TicketCommandService commandService;
    private final TicketQueryService queryService;
    private final UserRepository userRepository;
    private final SensorRepository sensorRepository;

    public ClientTicketController(TicketCommandService commandService, TicketQueryService queryService, UserRepository userRepository, SensorRepository sensorRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.userRepository = userRepository;
        this.sensorRepository = sensorRepository;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getClientTickets() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null ? auth.getName() : "anonimo@airq.com";
        
        List<TicketResponseDTO> tickets = queryService.getTicketsByClientEmail(email);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createTicket(@RequestBody ClientTicketRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth != null ? auth.getName() : "anonimo@airq.com";
        String clientName = email; // Fallback
        Long clientId = null;
        
        if (auth != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                clientId = user.getId();
                clientName = user.getSubscription() != null && user.getSubscription().getOrganizationName() != null 
                             ? user.getSubscription().getOrganizationName() 
                             : (user.getCompanyName() != null ? user.getCompanyName() : user.getUsername());
            }
        }

        // Build deviceId string from campus and classroom
        String deviceId = "N/A";
        if (clientId != null && request.campus() != null && !request.campus().isEmpty() && request.classroom() != null && !request.classroom().isEmpty()) {
            List<Sensor> sensors = sensorRepository.findByClientId(clientId);
            List<Sensor> filtered = sensors.stream()
                .filter(s -> request.campus().equals(s.getCampus()) && request.classroom().equals(s.getLocation()))
                .collect(Collectors.toList());
                
            if (!filtered.isEmpty()) {
                String macs = filtered.stream().map(Sensor::getSerialNumber).collect(Collectors.joining(", "));
                deviceId = "Sede: " + request.campus() + " | Aula: " + request.classroom() + "\nSensores: " + macs;
            } else {
                deviceId = "Sede: " + request.campus() + " | Aula: " + request.classroom() + "\n(Sin sensores asignados)";
            }
        } else if (request.campus() != null && !request.campus().isEmpty()) {
            deviceId = "Sede: " + request.campus();
            if (request.classroom() != null && !request.classroom().isEmpty()) {
                deviceId += " | Aula: " + request.classroom();
            }
        }

        // Assign Priority automatically
        String priority = "Bajo";
        if (request.category() != null) {
            String cat = request.category().toLowerCase();
            if (cat.contains("hardware")) {
                priority = "Alto";
            } else if (cat.contains("software")) {
                priority = "Medio";
            }
        }

        // Generate a random ticket number like #TK-A1B2
        String ticketNumber = "#TK-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        CreateTicketCommand command = new CreateTicketCommand(
            ticketNumber,
            clientName,
            email,
            request.category(),
            priority,
            deviceId,
            request.issueDescription()
        );

        String ticketId = commandService.createTicket(command);
        return ResponseEntity.ok(Map.of(
            "message", "Ticket creado con éxito",
            "ticketId", ticketId,
            "ticketNumber", ticketNumber
        ));
    }
}
