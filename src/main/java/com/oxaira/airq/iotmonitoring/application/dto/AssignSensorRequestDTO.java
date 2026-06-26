package com.oxaira.airq.iotmonitoring.application.dto;

public record AssignSensorRequestDTO(
        String macAddress,
        Long clientId,
        String campus,
        String location
) {
}
