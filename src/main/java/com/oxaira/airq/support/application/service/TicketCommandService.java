package com.oxaira.airq.support.application.service;

import com.oxaira.airq.support.application.command.AssignTechnicianCommand;
import com.oxaira.airq.support.application.command.CreateTicketCommand;
import com.oxaira.airq.support.domain.model.SupportTicket;
import com.oxaira.airq.support.infrastructure.persistence.SupportTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketCommandService {

    private final SupportTicketRepository repository;

    public TicketCommandService(SupportTicketRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String createTicket(CreateTicketCommand command) {
        SupportTicket ticket = new SupportTicket(
            command.ticketNumber(),
            command.clientName(),
            command.clientEmail(),
            command.category(),
            command.priority(),
            command.deviceId(),
            command.issueDescription()
        );
        repository.save(ticket);
        return ticket.getTicketId();
    }

    @Transactional
    public void assignTechnician(AssignTechnicianCommand command) {
        SupportTicket ticket = repository.findById(command.ticketId())
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        
        ticket.assignTechnician(command.technicianId());
        repository.save(ticket);
    }

    @Transactional
    public void resolveTicket(String ticketId) {
        SupportTicket ticket = repository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        
        ticket.resolve();
        repository.save(ticket);
    }
}
