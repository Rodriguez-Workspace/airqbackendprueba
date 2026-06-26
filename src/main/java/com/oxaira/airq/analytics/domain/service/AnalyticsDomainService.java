package com.oxaira.airq.analytics.domain.service;

import org.springframework.stereotype.Service;

@Service
public class AnalyticsDomainService {

    public String determineAirQualityStatus(
            Double averageCo2,
            Double averagePm25) {

        if (averageCo2 > 1000 || averagePm25 > 35) {
            return "BAJO";
        }

        if (averageCo2 > 700 || averagePm25 > 20) {
            return "MODERADO";
        }

        return "BUENO";
    }
}