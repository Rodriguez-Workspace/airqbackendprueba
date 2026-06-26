package com.oxaira.airq.machinelearning.application.service;

import com.oxaira.airq.machinelearning.infrastructure.client.PythonMLClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

// Disabled as predictions are now triggered real-time via MQTT Telemetry
// @Component
// @EnableScheduling
@RequiredArgsConstructor
public class PredictionJobScheduler {

    private final PredictionEngineService predictionEngineService;

    @Scheduled(fixedRate = 10000)
    public void runAnalysisJobs() {
        predictionEngineService.requestAnalysis("SN-8842", List.of(
                new PythonMLClient.MeasurementData(1050.0, 45.0, 24.5, 60.0),
                new PythonMLClient.MeasurementData(1100.0, 50.0, 25.0, 62.0)
        ));
        
        predictionEngineService.requestAnalysis("SN-4242", List.of(
                new PythonMLClient.MeasurementData(400.0, 10.0, 22.0, 50.0),
                new PythonMLClient.MeasurementData(410.0, 12.0, 22.5, 51.0)
        ));
    }
}
