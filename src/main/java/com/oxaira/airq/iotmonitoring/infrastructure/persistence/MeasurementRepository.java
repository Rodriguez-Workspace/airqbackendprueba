package com.oxaira.airq.iotmonitoring.infrastructure.persistence;

import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeasurementRepository
        extends JpaRepository<Measurement, Long> {

    List<Measurement> findBySensorId(Long sensorId);
    Measurement findTopBySensorIdOrderByRecordedAtDesc(Long sensorId);
    void deleteBySensorId(Long sensorId);

    @Query("SELECT new com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO(" +
           "AVG(m.co2), AVG(m.pm25), AVG(m.temperature), AVG(m.humidity)) " +
           "FROM Measurement m WHERE m.sensor.clientId = :clientId AND m.id IN (" +
           "  SELECT MAX(m2.id) FROM Measurement m2 GROUP BY m2.sensor.id" +
           ")")
    com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO getAverageMetricsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT m FROM Measurement m WHERE m.sensor.clientId = :clientId AND m.id IN (" +
           "  SELECT MAX(m2.id) FROM Measurement m2 GROUP BY m2.sensor.id" +
           ")")
    List<Measurement> getLatestMeasurementsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT m FROM Measurement m WHERE m.sensor.clientId = :clientId AND m.recordedAt >= :startDate ORDER BY m.recordedAt ASC")
    List<Measurement> getMeasurementsByClientIdAndDateAfter(@Param("clientId") Long clientId, @Param("startDate") java.time.LocalDateTime startDate);

    @Query("SELECT new com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO(" +
           "AVG(m.co2), AVG(m.pm25), AVG(m.temperature), AVG(m.humidity)) " +
           "FROM Measurement m WHERE m.sensor.clientId = :clientId AND m.sensor.campus = :campus AND m.id IN (" +
           "  SELECT MAX(m2.id) FROM Measurement m2 GROUP BY m2.sensor.id" +
           ")")
    com.oxaira.airq.iotmonitoring.application.dto.AverageMetricsDTO getAverageMetricsByClientIdAndCampus(@Param("clientId") Long clientId, @Param("campus") String campus);

    @Query("SELECT m FROM Measurement m WHERE m.sensor.clientId = :clientId AND m.sensor.campus = :campus AND m.recordedAt >= :startDate ORDER BY m.recordedAt ASC")
    List<Measurement> getMeasurementsByClientIdAndCampusAndDateAfter(@Param("clientId") Long clientId, @Param("campus") String campus, @Param("startDate") java.time.LocalDateTime startDate);
}