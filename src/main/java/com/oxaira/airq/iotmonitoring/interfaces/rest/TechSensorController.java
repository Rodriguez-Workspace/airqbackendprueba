package com.oxaira.airq.iotmonitoring.interfaces.rest;

import com.oxaira.airq.iotmonitoring.application.dto.AssignSensorRequestDTO;
import com.oxaira.airq.iotmonitoring.application.service.AssignSensorUseCase;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import com.oxaira.airq.iotmonitoring.application.service.DeleteSensorUseCase;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/tech/sensors")
@PreAuthorize("hasRole('TECHNICIAN')")
@RequiredArgsConstructor
public class TechSensorController {

    private final AssignSensorUseCase assignSensorUseCase;
    private final DeleteSensorUseCase deleteSensorUseCase;
    private final SensorRepository sensorRepository;

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<List<com.oxaira.airq.iotmonitoring.domain.model.Sensor>> getClientSensors(@PathVariable Long clientId) {
        return ResponseEntity.ok(sensorRepository.findByClientId(clientId));
    }

    @PostMapping("/assign")
    public ResponseEntity<Sensor> assignSensor(@RequestBody AssignSensorRequestDTO request) {
        Sensor sensor = assignSensorUseCase.execute(request);
        return ResponseEntity.ok(sensor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        deleteSensorUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
