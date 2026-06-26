package com.oxaira.airq.machinelearning.domain.service;

import org.springframework.stereotype.Service;

@Service
public class RiskEvaluationService {

    public String evaluateRisk(
            Double co2,
            Double pm25) {

        if (co2 > 1000 || pm25 > 35) {
            return "HIGH";
        }

        if (co2 > 700 || pm25 > 20) {
            return "MEDIUM";
        }

        return "LOW";
    }
}