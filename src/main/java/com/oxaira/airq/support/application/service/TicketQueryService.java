package com.oxaira.airq.support.application.service;

import com.oxaira.airq.support.application.dto.TicketResponseDTO;
import com.oxaira.airq.support.domain.model.TicketStatus;
import com.oxaira.airq.support.infrastructure.persistence.SupportTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketQueryService {

    private final SupportTicketRepository repository;

    public TicketQueryService(SupportTicketRepository repository) {
        this.repository = repository;
    }

    public List<TicketResponseDTO> getAllTickets(String status) {
        java.time.LocalDateTime startOfDay = java.time.LocalDate.now().atStartOfDay();

        if (status != null && !status.isEmpty()) {
            TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            return repository.findByStatus(ticketStatus).stream()
                .filter(t -> t.getStatus() != TicketStatus.RESOLVED || t.getResolvedAt() == null || !t.getResolvedAt().isBefore(startOfDay))
                .map(TicketResponseDTO::fromEntity)
                .collect(Collectors.toList());
        }
        return repository.findAll().stream()
            .filter(t -> t.getStatus() != TicketStatus.RESOLVED || t.getResolvedAt() == null || !t.getResolvedAt().isBefore(startOfDay))
            .map(TicketResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public List<TicketResponseDTO> getTicketsByClient(String clientName) {
        return repository.findByClientName(clientName).stream()
            .map(TicketResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public List<TicketResponseDTO> getTicketsByClientEmail(String clientEmail) {
        return repository.findByClientEmail(clientEmail).stream()
            .map(TicketResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public List<TicketResponseDTO> getTicketsByTechnicianId(String technicianId) {
        return repository.findByTechnicianId(technicianId).stream()
            .filter(t -> t.getStatus() != TicketStatus.RESOLVED)
            .map(TicketResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public TicketResponseDTO getTicketById(String ticketId) {
        return repository.findById(ticketId)
            .map(TicketResponseDTO::fromEntity)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }
}
