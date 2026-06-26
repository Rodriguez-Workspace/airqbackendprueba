package com.oxaira.airq.iotmonitoring.interfaces.rest;

import com.oxaira.airq.iotmonitoring.application.dto.CreateMeasurementRequest;
import com.oxaira.airq.iotmonitoring.application.service.CreateMeasurementUseCase;
import com.oxaira.airq.iotmonitoring.application.service.GetMeasurementsUseCase;
import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final CreateMeasurementUseCase createMeasurementUseCase;

    private final GetMeasurementsUseCase getMeasurementsUseCase;

    @PostMapping
    public Measurement create(
            @RequestBody CreateMeasurementRequest request) {

        return createMeasurementUseCase.execute(request);
    }

    @GetMapping
    public List<Measurement> getAll() {
        return getMeasurementsUseCase.execute();
    }

    @GetMapping("/sensor/{sensorId}")
    public List<Measurement> getBySensor(
            @PathVariable Long sensorId) {

        return getMeasurementsUseCase.execute(sensorId);
    }
}