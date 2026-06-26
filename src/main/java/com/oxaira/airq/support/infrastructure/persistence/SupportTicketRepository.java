package com.oxaira.airq.support.infrastructure.persistence;

import com.oxaira.airq.support.domain.model.SupportTicket;
import com.oxaira.airq.support.domain.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, String> {
    List<SupportTicket> findByStatus(TicketStatus status);
    List<SupportTicket> findByClientName(String clientName);
    List<SupportTicket> findByClientEmail(String clientEmail);
    List<SupportTicket> findByTechnicianId(String technicianId);
}
