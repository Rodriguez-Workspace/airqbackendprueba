package com.oxaira.airq.iotmonitoring.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxaira.airq.iotmonitoring.application.dto.CloudTelemetryDTO;
import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.MeasurementRepository;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SensorTelemetryAdapter {

    private final ObjectMapper objectMapper;
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final com.oxaira.airq.machinelearning.application.service.PredictionEngineService predictionEngineService;

    public SensorTelemetryAdapter(ObjectMapper objectMapper, SensorRepository sensorRepository, MeasurementRepository measurementRepository, com.oxaira.airq.machinelearning.application.service.PredictionEngineService predictionEngineService) {
        this.objectMapper = objectMapper;
        this.sensorRepository = sensorRepository;
        this.measurementRepository = measurementRepository;
        this.predictionEngineService = predictionEngineService;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        String payload = message.getPayload();
        System.out.println("Telemetry received via MQTT: " + payload);

        try {
            CloudTelemetryDTO dto = objectMapper.readValue(payload, CloudTelemetryDTO.class);
            String macId = dto.getHardwareMacId();

            Sensor sensor = sensorRepository.findBySerialNumber(macId).orElse(null);

            if (sensor != null) {
                Measurement measurement = Measurement.builder()
                        .co2(dto.getMetrics().getCo2())
                        .pm25(dto.getMetrics().getPm25())
                        .temperature(dto.getMetrics().getTemperature())
                        .humidity(dto.getMetrics().getHumidity())
                        .recordedAt(LocalDateTime.now())
                        .sensor(sensor)
                        .build();

                if (!Boolean.TRUE.equals(sensor.getActive())) {
                    sensor.setActive(true);
                    sensorRepository.save(sensor);
                }

                measurementRepository.save(measurement);
                System.out.println("Measurement saved successfully for sensor: " + macId);
                
                predictionEngineService.requestAnalysis(macId, java.util.List.of(
                     new com.oxaira.airq.machinelearning.infrastructure.client.PythonMLClient.MeasurementData(measurement.getCo2(), measurement.getPm25(), measurement.getTemperature(), measurement.getHumidity())
                ));
            } else {
                System.out.println("Warning: Received telemetry for unknown sensor MAC: " + macId);
            }
        } catch (Exception e) {
            System.err.println("Error parsing or saving MQTT telemetry: " + e.getMessage());
        }
    }
}