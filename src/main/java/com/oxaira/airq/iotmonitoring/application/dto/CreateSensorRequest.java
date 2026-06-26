package com.oxaira.airq.iotmonitoring.application.dto;

public record CreateSensorRequest(
        String serialNumber,
        String location) {
}