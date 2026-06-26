package com.oxaira.airq.iotmonitoring.application.command;

public record CreateMeasurementCommand(

        Double co2,

        Double pm25,

        Double temperature,

        Double humidity,

        Long sensorId

) {
}