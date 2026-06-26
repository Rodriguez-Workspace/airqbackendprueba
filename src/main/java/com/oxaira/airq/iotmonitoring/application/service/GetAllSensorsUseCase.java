package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllSensorsUseCase {

    private final SensorRepository sensorRepository;

    public List<Sensor> execute() {
        return sensorRepository.findAll();
    }
}