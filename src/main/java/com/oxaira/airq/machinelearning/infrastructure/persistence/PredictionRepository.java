package com.oxaira.airq.machinelearning.infrastructure.persistence;

import com.oxaira.airq.machinelearning.domain.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    List<Prediction> findTop10BySensorIdOrderByPredictedAtDesc(String sensorId);
}
