package com.oxaira.airq.analytics.domain.service;

public class AnalyticsCalculationService {

    public Double calculateAverage(
            double sum,
            long count) {

        if (count == 0) {
            return 0.0;
        }

        return sum / count;
    }
}