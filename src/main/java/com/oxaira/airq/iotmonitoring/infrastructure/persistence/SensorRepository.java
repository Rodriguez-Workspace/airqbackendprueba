package com.oxaira.airq.iotmonitoring.infrastructure.persistence;

import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository
        extends JpaRepository<Sensor, Long> {
    java.util.Optional<Sensor> findBySerialNumber(String serialNumber);
    java.util.List<Sensor> findByClientId(Long clientId);
}