package com.oxaira.airq.iotmonitoring.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class AirQualityPredictionAdapter {

    public String predictNextAirQualityLevel(
            Double co2,
            Double pm25) {

        if (co2 > 1000 || pm25 > 35) {
            return "RIESGO_ALTO";
        }

        if (co2 > 700 || pm25 > 20) {
            return "RIESGO_MEDIO";
        }

        return "RIESGO_BAJO";
    }
}