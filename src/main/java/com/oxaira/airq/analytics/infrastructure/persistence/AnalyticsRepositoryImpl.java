package com.oxaira.airq.analytics.infrastructure.persistence;

import com.oxaira.airq.analytics.domain.model.AnalyticsSummary;
import com.oxaira.airq.analytics.domain.repository.AnalyticsRepository;
import com.oxaira.airq.analytics.domain.service.AnalyticsDomainService;
import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnalyticsRepositoryImpl
        implements AnalyticsRepository {

    private final MeasurementRepository measurementRepository;

    private final AnalyticsDomainService analyticsDomainService;

    @Override
    public AnalyticsSummary generateDashboard() {

        List<Measurement> measurements = measurementRepository.findAll();

        if (measurements.isEmpty()) {

            return AnalyticsSummary.builder()
                    .averageCo2(0.0)
                    .averagePm25(0.0)
                    .averageTemperature(0.0)
                    .averageHumidity(0.0)
                    .totalMeasurements(0L)
                    .airQualityStatus("NO_DATA")
                    .build();
        }

        double avgCo2 = measurements.stream()
                .mapToDouble(Measurement::getCo2)
                .average()
                .orElse(0);

        double avgPm25 = measurements.stream()
                .mapToDouble(Measurement::getPm25)
                .average()
                .orElse(0);

        double avgTemp = measurements.stream()
                .mapToDouble(Measurement::getTemperature)
                .average()
                .orElse(0);

        double avgHumidity = measurements.stream()
                .mapToDouble(Measurement::getHumidity)
                .average()
                .orElse(0);

        String status = analyticsDomainService
                .determineAirQualityStatus(
                        avgCo2,
                        avgPm25);

        return AnalyticsSummary.builder()
                .averageCo2(avgCo2)
                .averagePm25(avgPm25)
                .averageTemperature(avgTemp)
                .averageHumidity(avgHumidity)
                .totalMeasurements(
                        (long) measurements.size())
                .airQualityStatus(status)
                .build();
    }
}