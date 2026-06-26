package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.application.dto.CreateMeasurementRequest;
import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.MeasurementRepository;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateMeasurementUseCase {

    private final MeasurementRepository measurementRepository;

    private final SensorRepository sensorRepository;

    public Measurement execute(CreateMeasurementRequest request) {

        Sensor sensor = sensorRepository.findById(request.sensorId())
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        Measurement measurement = Measurement.builder()
                .co2(request.co2())
                .pm25(request.pm25())
                .temperature(request.temperature())
                .humidity(request.humidity())
                .recordedAt(LocalDateTime.now())
                .sensor(sensor)
                .build();

        return measurementRepository.save(measurement);
    }
}