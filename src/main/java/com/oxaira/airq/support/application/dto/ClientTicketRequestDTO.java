package com.oxaira.airq.support.application.dto;

public record ClientTicketRequestDTO(
    String category,
    String issueDescription,
    String campus,
    String classroom
) {}
