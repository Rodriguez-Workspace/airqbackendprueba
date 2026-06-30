package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.iotmonitoring.domain.model.Measurement;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.MeasurementRepository;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import com.oxaira.airq.notifications.domain.model.NotificationEntity;
import com.oxaira.airq.notifications.infrastructure.persistence.NotificationEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class HardwareWatchdogService {

    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final NotificationEntityRepository notificationRepository;
    private final UserRepository userRepository;

    // Ejecuta cada 1 minuto (60000 ms)
    @Scheduled(fixedRate = 60000)
    public void checkSensorsHealth() {
        List<Sensor> sensors = sensorRepository.findAll();
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);

        for (Sensor sensor : sensors) {
            if (Boolean.TRUE.equals(sensor.getActive())) {
                Measurement lastMeasurement = measurementRepository.findTopBySensorIdOrderByRecordedAtDesc(sensor.getId());

                if (lastMeasurement != null && lastMeasurement.getRecordedAt().isBefore(threshold)) {
                    // El sensor lleva más de 5 minutos sin emitir datos
                    sensor.setActive(false);
                    sensorRepository.save(sensor);

                    // Generar notificación de HARDWARE_FAILURE
                    generateFailureNotification(sensor);
                }
            }
        }
    }

    private void generateFailureNotification(Sensor sensor) {
        if (sensor.getClientId() != null) {
            User client = userRepository.findById(sensor.getClientId()).orElse(null);
            if (client != null) {
                String location = sensor.getLocation() != null ? sensor.getLocation() : "Desconocida";
                
                NotificationEntity lastNotification = notificationRepository.findFirstByClientAndLocationAndTypeOrderByCreatedAtDesc(client, location, "HARDWARE_FAILURE");
                
                boolean shouldSave = true;
                if (lastNotification != null 
                    && java.time.temporal.ChronoUnit.MINUTES.between(lastNotification.getCreatedAt(), LocalDateTime.now()) < 15) {
                    shouldSave = false; // Debounce: 15 min rule for hardware failure
                }

                if (shouldSave) {
                    NotificationEntity notification = NotificationEntity.builder()
                            .client(client)
                            .type("HARDWARE_FAILURE")
                            .location(location)
                            .diagnosis("Falla de Conexión: El sensor " + sensor.getSerialNumber() + " dejó de transmitir datos.")
                            .executedAction("Revisión técnica requerida.")
                            .isRead(false)
                            .createdAt(LocalDateTime.now())
                            .build();

                    notificationRepository.save(notification);
                    System.out.println("Hardware failure detected for sensor: " + sensor.getSerialNumber());
                }
            }
        }
    }
}
