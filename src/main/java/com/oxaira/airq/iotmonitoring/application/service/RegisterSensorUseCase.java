package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.application.dto.CreateSensorRequest;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterSensorUseCase {

    private final SensorRepository sensorRepository;

    public Sensor execute(CreateSensorRequest request) {

        Sensor sensor = Sensor.builder()
                .serialNumber(request.serialNumber())
                .location(request.location())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return sensorRepository.save(sensor);
    }
}