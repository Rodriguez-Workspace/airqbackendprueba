package com.oxaira.airq.support.application.dto;

import com.oxaira.airq.support.domain.model.SupportTicket;
import java.time.LocalDateTime;

public record TicketResponseDTO(
    String ticketId,
    String ticketNumber,
    String clientName,
    String clientEmail,
    String category,
    String priority,
    String deviceId,
    String technicianId,
    String issueDescription,
    String status,
    LocalDateTime createdAt,
    LocalDateTime resolvedAt
) {
    public static TicketResponseDTO fromEntity(SupportTicket ticket) {
        return new TicketResponseDTO(
            ticket.getTicketId(),
            ticket.getTicketNumber(),
            ticket.getClientName(),
            ticket.getClientEmail(),
            ticket.getCategory(),
            ticket.getPriority(),
            ticket.getDeviceId(),
            ticket.getTechnicianId(),
            ticket.getIssueDescription(),
            ticket.getStatus().name(),
            ticket.getCreatedAt(),
            ticket.getResolvedAt()
        );
    }
}
