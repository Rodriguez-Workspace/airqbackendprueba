package com.oxaira.airq.support.interfaces.rest;

import com.oxaira.airq.support.application.command.AssignTechnicianCommand;
import com.oxaira.airq.support.application.command.CreateTicketCommand;
import com.oxaira.airq.support.application.dto.TicketResponseDTO;
import com.oxaira.airq.support.application.service.TicketCommandService;
import com.oxaira.airq.support.application.service.TicketQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/tickets")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTicketController {

    private final TicketQueryService queryService;
    private final TicketCommandService commandService;

    public AdminTicketController(TicketQueryService queryService, TicketCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(queryService.getAllTickets(status));
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createTicket(@RequestBody CreateTicketCommand command) {
        CreateTicketCommand cmdWithEmail = new CreateTicketCommand(
            command.ticketNumber(),
            command.clientName(),
            command.clientEmail() != null ? command.clientEmail() : "",
            command.category(),
            command.priority(),
            command.deviceId(),
            command.issueDescription()
        );
        String ticketId = commandService.createTicket(cmdWithEmail);
        return ResponseEntity.ok(Map.of("message", "Ticket creado con éxito", "ticketId", ticketId));
    }

    @PutMapping("/{ticketId}/assign")
    public ResponseEntity<Map<String, String>> assignTechnician(@PathVariable String ticketId, @RequestBody Map<String, String> body) {
        String technicianId = body.get("technicianId");
        if (technicianId == null || technicianId.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Se requiere el technicianId"));
        }
        commandService.assignTechnician(new AssignTechnicianCommand(ticketId, technicianId));
        return ResponseEntity.ok(Map.of("message", "Técnico asignado y ticket en progreso"));
    }

    @PutMapping("/{ticketId}/resolve")
    public ResponseEntity<Map<String, String>> resolveTicket(@PathVariable String ticketId) {
        commandService.resolveTicket(ticketId);
        return ResponseEntity.ok(Map.of("message", "Ticket resuelto exitosamente"));
    }
}
