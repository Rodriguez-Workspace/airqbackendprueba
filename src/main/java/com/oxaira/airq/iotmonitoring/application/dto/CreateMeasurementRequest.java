package com.oxaira.airq.iotmonitoring.application.dto;

public record CreateMeasurementRequest(

        Double co2,

        Double pm25,

        Double temperature,

        Double humidity,

        Long sensorId

) {
}