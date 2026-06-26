package com.oxaira.airq.machinelearning.domain.service;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    public String generateRecommendation(
            String riskLevel) {

        return switch (riskLevel) {

            case "HIGH" ->
                "Immediate ventilation required";

            case "MEDIUM" ->
                "Monitor environmental conditions";

            default ->
                "Air quality is acceptable";
        };
    }
}