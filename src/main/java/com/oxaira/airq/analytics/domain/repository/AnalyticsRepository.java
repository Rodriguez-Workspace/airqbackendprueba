package com.oxaira.airq.analytics.domain.repository;

import com.oxaira.airq.analytics.domain.model.AnalyticsSummary;

public interface AnalyticsRepository {

    AnalyticsSummary generateDashboard();

}