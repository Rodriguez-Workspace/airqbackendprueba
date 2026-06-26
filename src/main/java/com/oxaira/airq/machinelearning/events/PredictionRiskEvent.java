package com.oxaira.airq.machinelearning.events;

public record PredictionRiskEvent(
    String sensorId, 
    String riskLevel, 
    String aiActionTaken
) {}
