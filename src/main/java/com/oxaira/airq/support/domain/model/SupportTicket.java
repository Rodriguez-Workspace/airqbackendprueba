package com.oxaira.airq.support.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @Column(name = "ticket_id")
    private String ticketId;

    @Column(name = "ticket_number", unique = true)
    private String ticketNumber;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_email")
    private String clientEmail;

    @Column(name = "category")
    private String category;

    @Column(name = "priority")
    private String priority;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "technician_id")
    private String technicianId;

    @Column(name = "issue_description")
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    protected SupportTicket() {}

    public SupportTicket(String ticketNumber, String clientName, String clientEmail, String category, String priority, String deviceId, String issueDescription) {
        this.ticketId = UUID.randomUUID().toString();
        this.ticketNumber = ticketNumber;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.category = category;
        this.priority = priority;
        this.deviceId = deviceId;
        this.issueDescription = issueDescription;
        this.status = TicketStatus.OPEN;
        this.createdAt = LocalDateTime.now();
    }

    public void assignTechnician(String technicianId) {
        this.technicianId = technicianId;
        this.status = TicketStatus.IN_PROGRESS;
    }

    public void resolve() {
        this.status = TicketStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    public String getTicketId() { return ticketId; }
    public String getTicketNumber() { return ticketNumber; }
    public String getClientName() { return clientName; }
    public String getClientEmail() { return clientEmail; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getDeviceId() { return deviceId; }
    public String getTechnicianId() { return technicianId; }
    public String getIssueDescription() { return issueDescription; }
    public TicketStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
}
