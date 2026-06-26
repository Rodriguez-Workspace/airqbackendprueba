package com.oxaira.airq.notifications.infrastructure.persistence;

import com.oxaira.airq.machinelearning.application.dto.PredictionResponse;
import com.oxaira.airq.notifications.domain.model.Notification;
import com.oxaira.airq.notifications.domain.repository.NotificationRepository;
import com.oxaira.airq.notifications.domain.service.NotificationMessageService;
import com.oxaira.airq.notifications.infrastructure.adapter.MachineLearningAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl
        implements NotificationRepository {

    private final MachineLearningAdapter machineLearningAdapter;

    private final NotificationMessageService messageService;

    @Override
    public Notification generateNotification() {

        PredictionResponse prediction = machineLearningAdapter.getPrediction();

        String message = messageService.generateMessage(
                prediction.riskLevel());

        return Notification.builder()
                .title("AirQ Alert")
                .message(message)
                .severity(prediction.riskLevel())
                .createdAt(LocalDateTime.now())
                .build();
    }
}