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

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client/tickets")
@PreAuthorize("hasRole('CLIENT') or hasRole('USER')")
public class ClientTicketController {

    private final TicketCommandService commandService;
    private final TicketQueryService queryService;
    private final UserRepository userRepository;

    public ClientTicketController(TicketCommandService commandService, TicketQueryService queryService, UserRepository userRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.userRepository = userRepository;
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
        
        if (auth != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                clientName = user.getUsername();
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
            null, // deviceId not used by clients anymore
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
