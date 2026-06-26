package com.oxaira.airq.machinelearning.infrastructure.adapter;

import com.oxaira.airq.analytics.domain.model.AnalyticsSummary;
import com.oxaira.airq.analytics.domain.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsAdapter {

    private final AnalyticsRepository analyticsRepository;

    public AnalyticsSummary getAnalyticsSummary() {
        return analyticsRepository.generateDashboard();
    }
}