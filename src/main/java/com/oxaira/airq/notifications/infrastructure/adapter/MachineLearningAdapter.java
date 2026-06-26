package com.oxaira.airq.notifications.infrastructure.adapter;

import com.oxaira.airq.machinelearning.application.dto.PredictionResponse;
import com.oxaira.airq.machinelearning.application.service.GeneratePredictionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MachineLearningAdapter {

    private final GeneratePredictionUseCase predictionUseCase;

    public PredictionResponse getPrediction() {

        return predictionUseCase.execute();

    }
}