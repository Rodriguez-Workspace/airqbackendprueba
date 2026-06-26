package com.oxaira.airq.iotmonitoring.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxaira.airq.iotmonitoring.application.dto.ActuatorCommand;
import com.oxaira.airq.iotmonitoring.infrastructure.adapter.ActuatorCommandProducer;
import com.oxaira.airq.machinelearning.events.PredictionRiskEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PredictionEventListener {

    private final ActuatorCommandMapper actuatorCommandMapper;
    private final ActuatorCommandProducer actuatorCommandProducer;
    private final ObjectMapper objectMapper;

    public PredictionEventListener(ActuatorCommandMapper actuatorCommandMapper, ActuatorCommandProducer actuatorCommandProducer, ObjectMapper objectMapper) {
        this.actuatorCommandMapper = actuatorCommandMapper;
        this.actuatorCommandProducer = actuatorCommandProducer;
        this.objectMapper = objectMapper;
    }

    @Async
    @EventListener
    public void handlePredictionRiskEvent(PredictionRiskEvent event) {
        try {
            System.out.println("Processing PredictionRiskEvent asynchronously for sensor: " + event.sensorId());
            
            // 1. Mapear la instrucción de texto de la IA a la estructura de control de hardware
            ActuatorCommand command = actuatorCommandMapper.mapAiActionToCommand(event.aiActionTaken());
            
            // 2. Serializar a JSON
            String payload = objectMapper.writeValueAsString(command);
            
            // 3. Publicar comando de vuelta al ESP32 (Vía MQTT Outbound Channel)
            actuatorCommandProducer.sendCommand(payload);
            
            System.out.println("Actuator command published successfully to MQTT for sensor: " + event.sensorId() + ". Command: " + payload);

        } catch (Exception e) {
            System.err.println("Failed to process PredictionRiskEvent or publish MQTT command for sensor " + event.sensorId() + ". Error: " + e.getMessage());
            // No arrojamos la excepción para no romper flujos principales, solo logueamos.
        }
    }
}
