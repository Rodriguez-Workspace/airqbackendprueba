package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMeasurementsUseCase {

    private final MeasurementRepository repository;

    public List<Measurement> execute() {
        return repository.findAll();
    }

    public List<Measurement> execute(Long sensorId) {
        return repository.findBySensorId(sensorId);
    }
}