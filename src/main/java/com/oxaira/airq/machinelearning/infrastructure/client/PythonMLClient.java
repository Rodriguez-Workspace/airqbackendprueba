package com.oxaira.airq.machinelearning.infrastructure.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.List;

@Slf4j
@Component
public class PythonMLClient {

    private final RestTemplate restTemplate;

    @Value("${ml.api.url:https://ml-iotprueba.onrender.com/predict}")
    private String mlApiUrl;

    public PythonMLClient() {
        this.restTemplate = new RestTemplate();
    }

    public PredictionResponse getPrediction(String sensorId, List<MeasurementData> recentData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PredictionRequest> request = new HttpEntity<>(new PredictionRequest(sensorId, recentData), headers);
            
            ResponseEntity<PredictionResponse> response = restTemplate.postForEntity(
                    mlApiUrl,
                    request,
                    PredictionResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.warn("Error calling python ML service at {}: {}", mlApiUrl, e.getMessage());
            // Fallback for simulation/testing if python server is offline
            double avgCo2 = recentData.stream().mapToDouble(MeasurementData::co2).average().orElse(0.0);
            String risk = avgCo2 > 1000 ? "CRITICAL" : "LOW";
            String action = avgCo2 > 1000 ? "Sistema de ventilación activado al 70%" : "Ninguna";
            return new PredictionResponse(risk, action);
        }
    }

    public record MeasurementData(Double co2, Double pm25, Double temp, Double hum) {}
    public record PredictionRequest(String sensorId, List<MeasurementData> data) {}
    public record PredictionResponse(String riskLevel, String aiActionTaken) {}
}
