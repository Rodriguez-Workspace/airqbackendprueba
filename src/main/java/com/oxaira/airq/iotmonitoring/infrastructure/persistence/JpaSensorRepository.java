package com.oxaira.airq.iotmonitoring.infrastructure.persistence;

import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSensorRepository
        extends JpaRepository<Sensor, Long> {
}