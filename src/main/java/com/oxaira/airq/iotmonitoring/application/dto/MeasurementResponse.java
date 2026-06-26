package com.oxaira.airq.iotmonitoring.application.dto;

import java.time.LocalDateTime;

public record MeasurementResponse(

        Long id,

        Double co2,

        Double pm25,

        Double temperature,

        Double humidity,

        LocalDateTime recordedAt,

        Long sensorId

) {
}