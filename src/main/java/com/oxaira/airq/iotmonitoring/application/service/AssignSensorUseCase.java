package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.application.dto.AssignSensorRequestDTO;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssignSensorUseCase {

    private final SensorRepository sensorRepository;

    public Sensor execute(AssignSensorRequestDTO request) {
        Sensor sensor = sensorRepository.findBySerialNumber(request.macAddress()).orElse(null);

        if (sensor == null) {
            sensor = Sensor.builder()
                    .serialNumber(request.macAddress())
                    .campus(request.campus())
                    .location(request.location())
                    .clientId(request.clientId())
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            sensor.setCampus(request.campus());
            sensor.setLocation(request.location());
            sensor.setClientId(request.clientId());
        }

        return sensorRepository.save(sensor);
    }
}
