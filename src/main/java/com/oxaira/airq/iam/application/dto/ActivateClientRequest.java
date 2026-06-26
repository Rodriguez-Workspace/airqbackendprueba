package com.oxaira.airq.iam.application.dto;

import com.oxaira.airq.subscription.domain.model.Plan;

public record ActivateClientRequest(
        Plan plan,
        String organizationName,
        Integer initialSensorsCount
) {
}
