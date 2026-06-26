package com.oxaira.airq.machinelearning.application.service;

import com.oxaira.airq.machinelearning.domain.model.Prediction;
import com.oxaira.airq.machinelearning.events.PredictionRiskEvent;
import com.oxaira.airq.machinelearning.infrastructure.client.PythonMLClient;
import com.oxaira.airq.machinelearning.infrastructure.client.PythonMLClient.PredictionResponse;
import com.oxaira.airq.machinelearning.infrastructure.persistence.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionEngineService {

    private final PredictionRepository predictionRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PythonMLClient pythonMLClient;

    public void requestAnalysis(String sensorId, List<PythonMLClient.MeasurementData> recentMeasurements) {
        PredictionResponse response = pythonMLClient.getPrediction(sensorId, recentMeasurements);

        Prediction prediction = Prediction.builder()
                .sensorId(sensorId)
                .predictedValue(0.0) // No longer provided by python model
                .riskLevel(response.riskLevel())
                .aiActionTaken(response.aiActionTaken())
                .predictedAt(LocalDateTime.now())
                .build();

        predictionRepository.save(prediction);

        eventPublisher.publishEvent(new PredictionRiskEvent(
                sensorId,
                response.riskLevel(),
                response.aiActionTaken()
        ));
    }
}
