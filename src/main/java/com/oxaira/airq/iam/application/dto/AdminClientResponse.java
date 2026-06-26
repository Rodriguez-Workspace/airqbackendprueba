package com.oxaira.airq.iam.application.dto;

public record AdminClientResponse(
        Long id,
        String institutionName,
        String contactName,
        String contactEmail,
        String subscriptionPlan,
        Integer hardwareSensorsCount,
        Integer hardwareSensorsLimit,
        Double monthlyBilling,
        String status
) {
}
