package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.application.dto.CreateSensorRequest;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSensorUseCase {

    private final SensorRepository sensorRepository;

    public Sensor execute(
            Long id,
            CreateSensorRequest request) {

        Sensor sensor = sensorRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Sensor no encontrado"));

        sensor.setSerialNumber(
                request.serialNumber());

        sensor.setLocation(
                request.location());

        return sensorRepository.save(
                sensor);

    }

}