package com.oxaira.airq.analytics.application.service;

import com.oxaira.airq.analytics.application.dto.DashboardResponse;
import com.oxaira.airq.analytics.domain.model.AnalyticsSummary;
import com.oxaira.airq.analytics.domain.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateDashboardUseCase {

    private final AnalyticsRepository analyticsRepository;

    public DashboardResponse execute() {

        AnalyticsSummary summary = analyticsRepository.generateDashboard();

        return new DashboardResponse(
                summary.getAverageCo2(),
                summary.getAveragePm25(),
                summary.getAverageTemperature(),
                summary.getAverageHumidity(),
                summary.getTotalMeasurements(),
                summary.getAirQualityStatus());
    }
}