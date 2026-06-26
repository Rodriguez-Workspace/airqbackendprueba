package com.oxaira.airq.analytics.application.dto;

public record SensorAnalyticsResponse(

        Long sensorId,

        Double averageCo2,

        Double averagePm25,

        Double averageTemperature,

        Double averageHumidity,

        Long totalMeasurements

) {
}