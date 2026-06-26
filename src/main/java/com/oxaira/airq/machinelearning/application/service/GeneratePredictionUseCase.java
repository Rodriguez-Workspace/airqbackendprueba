package com.oxaira.airq.machinelearning.application.service;

import com.oxaira.airq.machinelearning.application.dto.PredictionResponse;
import com.oxaira.airq.machinelearning.domain.model.Prediction;
import com.oxaira.airq.machinelearning.infrastructure.persistence.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneratePredictionUseCase {

    private final PredictionRepository predictionRepository;

    public PredictionResponse execute() {
        // Obtenemos la última predicción general (para evitar romper el controlador GET)
        List<Prediction> predictions = predictionRepository.findAll();
        if (predictions.isEmpty()) {
            return new PredictionResponse("Unknown", 0.0, "LOW", "Ninguna");
        }
        Prediction latest = predictions.get(predictions.size() - 1);
        return new PredictionResponse(
                latest.getSensorId(),
                latest.getPredictedValue(),
                latest.getRiskLevel(),
                latest.getAiActionTaken());
    }
}