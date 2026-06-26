package com.oxaira.airq.support.application.command;

public record AssignTechnicianCommand(
    String ticketId,
    String technicianId
) {}
