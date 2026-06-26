package com.oxaira.airq.analytics.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsSummary {

    private Double averageCo2;

    private Double averagePm25;

    private Double averageTemperature;

    private Double averageHumidity;

    private Long totalMeasurements;

    private String airQualityStatus;
}