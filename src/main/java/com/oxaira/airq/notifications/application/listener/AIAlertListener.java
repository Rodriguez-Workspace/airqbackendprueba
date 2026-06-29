package com.oxaira.airq.notifications.application.listener;

import com.oxaira.airq.iam.domain.model.User;
import com.oxaira.airq.iam.infrastructure.persistence.UserRepository;
import com.oxaira.airq.iotmonitoring.domain.model.Sensor;
import com.oxaira.airq.iotmonitoring.infrastructure.persistence.SensorRepository;
import com.oxaira.airq.machinelearning.events.PredictionRiskEvent;
import com.oxaira.airq.notifications.domain.model.NotificationEntity;
import com.oxaira.airq.notifications.infrastructure.persistence.NotificationEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class AIAlertListener {

    private final NotificationEntityRepository notificationRepository;
    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;

    @EventListener
    public void handlePredictionRiskEvent(PredictionRiskEvent event) {
        if ("CRITICAL".equals(event.riskLevel()) || "MEDIUM".equals(event.riskLevel())) {
            
            Sensor sensor = sensorRepository.findBySerialNumber(event.sensorId()).orElse(null);
            
            if (sensor != null && sensor.getClientId() != null) {
                User client = userRepository.findById(sensor.getClientId()).orElse(null);
                
                if (client != null) {
                    String location = sensor.getLocation() != null ? sensor.getLocation() : "Desconocida";
                    String executedAction = event.aiActionTaken();
                    
                    NotificationEntity lastNotification = notificationRepository.findFirstByClientAndLocationOrderByCreatedAtDesc(client, location);
                    
                    boolean shouldSave = true;
                    if (lastNotification != null 
                        && executedAction.equals(lastNotification.getExecutedAction()) 
                        && ChronoUnit.MINUTES.between(lastNotification.getCreatedAt(), LocalDateTime.now()) < 15) {
                        shouldSave = false; // Debounce: Ya hay una alerta idéntica reciente
                    }

                    if (shouldSave) {
                        NotificationEntity notification = NotificationEntity.builder()
                                .client(client)
                                .type("AI_ACTION")
                                .location(location)
                                .diagnosis(event.riskLevel() + ": Detección algorítmica de riesgo.")
                                .executedAction(executedAction)
                                .isRead(false)
                                .createdAt(LocalDateTime.now())
                                .build();

                        notificationRepository.save(notification);
                    }
                }
            }
        }
    }
}
