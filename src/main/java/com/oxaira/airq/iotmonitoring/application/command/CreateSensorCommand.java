package com.oxaira.airq.iotmonitoring.application.command;

public record CreateSensorCommand(
        String serialNumber,
        String location) {
}