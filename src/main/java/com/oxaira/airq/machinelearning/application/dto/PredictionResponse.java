package com.oxaira.airq.machinelearning.application.dto;

public record PredictionResponse(
        String sensorId,
        Double predictedValue,
        String riskLevel,
        String aiActionTaken
) {}
