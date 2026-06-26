package com.oxaira.airq.iotmonitoring.interfaces.rest;

import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/client/sensors")
@PreAuthorize("hasRole('CLIENT')")
@RequiredArgsConstructor
public class ClientSensorController {

    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;
    private final com.oxaira.airq.iotmonitoring.infrastructure.persistence.MeasurementRepository measurementRepository;

    @GetMapping
    public ResponseEntity<List<Sensor>> getMySensors(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Sensor> sensors = sensorRepository.findByClientId(user.getId());
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/metrics/average")
    public ResponseEntity<com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO> getAverageMetrics(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        var metrics = measurementRepository.getAverageMetricsByClientId(user.getId());
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/status")
    public ResponseEntity<List<com.oxaira.airq.iotmonitoring.application.dto.SensorStatusDTO>> getSensorStatus(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<Sensor> allSensors = sensorRepository.findByClientId(user.getId());
        List<com.oxaira.airq.iotmonitoring.domain.model.Measurement> latestMeasurements = measurementRepository.getLatestMeasurementsByClientId(user.getId());

        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        List<com.oxaira.airq.iotmonitoring.application.dto.SensorStatusDTO> result = allSensors.stream().map(sensor -> {
            var measurement = latestMeasurements.stream()
                .filter(m -> m.getSensor().getId().equals(sensor.getId()))
                .findFirst()
                .orElse(null);

            boolean isOnline = false;
            Double co2 = null;
            Double pm25 = null;
            java.time.LocalDateTime lastSeen = null;

            if (measurement != null) {
                lastSeen = measurement.getRecordedAt();
                // If the latest measurement is within 5 minutes, it is online
                if (lastSeen != null && java.time.Duration.between(lastSeen, now).toMinutes() <= 5) {
                    isOnline = true;
                }
                co2 = measurement.getCo2();
                pm25 = measurement.getPm25();
            }

            return new com.oxaira.airq.iotmonitoring.application.dto.SensorStatusDTO(
                sensor.getId(),
                sensor.getSerialNumber(),
                sensor.getCampus(),
                sensor.getLocation(),
                isOnline,
                co2,
                pm25,
                lastSeen
            );
        }).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/metrics/historical")
    public ResponseEntity<List<com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO>> getHistoricalMetrics(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Get measurements from the last 24 hours
        java.time.LocalDateTime startDate = java.time.LocalDateTime.now().minusHours(24);
        List<com.oxaira.airq.iotmonitoring.domain.model.Measurement> measurements = measurementRepository.getMeasurementsByClientIdAndDateAfter(user.getId(), startDate);

        // Group by hour
        java.util.Map<Integer, List<com.oxaira.airq.iotmonitoring.domain.model.Measurement>> groupedByHour = measurements.stream()
                .collect(java.util.stream.Collectors.groupingBy(m -> m.getRecordedAt().getHour()));

        List<com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO> historicalData = new java.util.ArrayList<>();
        
        // Ensure all 24 hours are present in the response
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (int i = 23; i >= 0; i--) {
            java.time.LocalDateTime hourTime = now.minusHours(i);
            int hour = hourTime.getHour();
            String hourString = String.format("%02d:00", hour);

            List<com.oxaira.airq.iotmonitoring.domain.model.Measurement> hourMeasurements = groupedByHour.getOrDefault(hour, Collections.emptyList());

            if (hourMeasurements.isEmpty()) {
                historicalData.add(new com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO(hourString, 0.0, 0.0, 0.0, 0.0));
            } else {
                double avgCo2 = hourMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getCo2).average().orElse(0.0);
                double avgPm25 = hourMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getPm25).average().orElse(0.0);
                double avgTemp = hourMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getTemperature).average().orElse(0.0);
                double avgHum = hourMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getHumidity).average().orElse(0.0);
                
                historicalData.add(new com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO(hourString, avgCo2, avgPm25, avgTemp, avgHum));
            }
        }

        return ResponseEntity.ok(historicalData);
    }
}
