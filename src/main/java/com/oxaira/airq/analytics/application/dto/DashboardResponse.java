package com.oxaira.airq.analytics.application.dto;

public record DashboardResponse(

        Double averageCo2,

        Double averagePm25,

        Double averageTemperature,

        Double averageHumidity,

        Long totalMeasurements,

        String airQualityStatus

) {
}