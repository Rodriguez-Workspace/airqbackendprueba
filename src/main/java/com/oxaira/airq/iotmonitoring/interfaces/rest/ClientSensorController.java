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
    public ResponseEntity<com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO> getAverageMetrics(
            Authentication authentication,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String campus) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        
        com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO metrics;
        if (campus != null && !campus.isEmpty()) {
            metrics = measurementRepository.getAverageMetricsByClientIdAndCampus(user.getId(), campus);
        } else {
            metrics = measurementRepository.getAverageMetricsByClientId(user.getId());
        }
        
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
    public ResponseEntity<List<com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO>> getHistoricalMetrics(
            Authentication authentication,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "0") int offset,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String campus) {
        
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Get client's current time
        java.time.LocalDateTime serverNow = java.time.LocalDateTime.now(java.time.ZoneId.of("UTC"));
        java.time.LocalDateTime clientNow = serverNow.minusMinutes(offset);

        // Get measurements from the beginning of client's today (00:00)
        java.time.LocalDateTime clientStartDate = clientNow.toLocalDate().atStartOfDay();
        
        // Convert client's 00:00 back to server time for the DB query
        java.time.LocalDateTime serverStartDate = clientStartDate.plusMinutes(offset);

        List<com.oxaira.airq.iotmonitoring.domain.model.Measurement> measurements;
        if (campus != null && !campus.isEmpty()) {
            measurements = measurementRepository.getMeasurementsByClientIdAndCampusAndDateAfter(user.getId(), campus, serverStartDate);
        } else {
            measurements = measurementRepository.getMeasurementsByClientIdAndDateAfter(user.getId(), serverStartDate);
        }

        // Group by client's 1-minute slot (1440 slots per day)
        java.util.Map<Integer, List<com.oxaira.airq.iotmonitoring.domain.model.Measurement>> groupedBySlot = measurements.stream()
                .collect(java.util.stream.Collectors.groupingBy(m -> {
                    java.time.LocalDateTime localTime = m.getRecordedAt().minusMinutes(offset);
                    return localTime.getHour() * 60 + localTime.getMinute();
                }));

        List<com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO> historicalData = new java.util.ArrayList<>();
        
        // Ensure all 1440 slots of today (00:00 to 23:59) are present in the response
        for (int i = 0; i < 1440; i++) {
            int hour = i / 60;
            int min = i % 60;
            String timeString = String.format("%02d:%02d", hour, min);

            List<com.oxaira.airq.iotmonitoring.domain.model.Measurement> slotMeasurements = groupedBySlot.getOrDefault(i, Collections.emptyList());

            if (slotMeasurements.isEmpty()) {
                historicalData.add(new com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO(timeString, null, null, null, null));
            } else {
                double avgCo2 = slotMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getCo2).average().orElse(0.0);
                double avgPm25 = slotMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getPm25).average().orElse(0.0);
                double avgTemp = slotMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getTemperature).average().orElse(0.0);
                double avgHum = slotMeasurements.stream().mapToDouble(com.oxaira.airq.iotmonitoring.domain.model.Measurement::getHumidity).average().orElse(0.0);
                
                historicalData.add(new com.oxaira.airq.iotmonitoring.application.dto.HourlyMetricDTO(timeString, avgCo2, avgPm25, avgTemp, avgHum));
            }
        }

        return ResponseEntity.ok(historicalData);
    }
}
