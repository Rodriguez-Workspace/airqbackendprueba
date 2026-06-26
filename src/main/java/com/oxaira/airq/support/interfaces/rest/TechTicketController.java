package com.oxaira.airq.support.interfaces.rest;

import com.oxaira.airq.support.application.dto.TicketResponseDTO;
import com.oxaira.airq.support.application.service.TicketCommandService;
import com.oxaira.airq.support.application.service.TicketQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tech/tickets")
@PreAuthorize("hasRole('TECHNICIAN')")
public class TechTicketController {

    private final TicketQueryService queryService;
    private final TicketCommandService commandService;
    private final com.oxaira.airq.iam.infrastructure.persistence.UserRepository userRepository;

    public TechTicketController(TicketQueryService queryService, TicketCommandService commandService, com.oxaira.airq.iam.infrastructure.persistence.UserRepository userRepository) {
        this.queryService = queryService;
        this.commandService = commandService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getMyTickets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        com.oxaira.airq.iam.domain.model.User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        String technicianId = String.valueOf(user.getId());
        return ResponseEntity.ok(queryService.getTicketsByTechnicianId(technicianId));
    }

    @PutMapping("/{ticketId}/resolve")
    public ResponseEntity<Map<String, String>> resolveTicket(@PathVariable String ticketId) {
        commandService.resolveTicket(ticketId);
        return ResponseEntity.ok(Map.of("message", "Ticket resuelto exitosamente"));
    }
}
