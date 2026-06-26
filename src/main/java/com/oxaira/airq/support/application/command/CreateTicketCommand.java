package com.oxaira.airq.support.application.command;

public record CreateTicketCommand(
    String ticketNumber,
    String clientName,
    String clientEmail,
    String category,
    String priority,
    String deviceId,
    String issueDescription
) {}
