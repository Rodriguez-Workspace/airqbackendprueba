package com.oxaira.airq.iotmonitoring.domain.service;

public class AirQualityEvaluationService {

    public String evaluate(
            Double co2,
            Double pm25) {

        if (co2 == null || pm25 == null) {
            return "DESCONOCIDO";
        }

        if (co2 > 1000 || pm25 > 35) {
            return "BAJO";
        }

        if (co2 > 700 || pm25 > 20) {
            return "MODERADO";
        }

        return "BUENO";
    }
}