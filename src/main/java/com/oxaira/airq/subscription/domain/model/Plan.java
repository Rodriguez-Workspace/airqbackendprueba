package com.oxaira.airq.subscription.domain.model;

import lombok.Getter;

@Getter
public enum Plan {
    BASIC("Plan AirQ Básico", 1200.00, 20, 0.0),
    PRO("Plan AirQ Pro", 2500.00, 50, 45.0);

    private final String name;
    private final Double basePrice;
    private final Integer baseSensorLimit;
    private final Double extraSensorPrice;

    Plan(String name, Double basePrice, Integer baseSensorLimit, Double extraSensorPrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.baseSensorLimit = baseSensorLimit;
        this.extraSensorPrice = extraSensorPrice;
    }
}
