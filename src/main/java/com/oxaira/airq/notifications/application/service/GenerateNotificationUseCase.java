package com.oxaira.airq.notifications.application.service;

import com.oxaira.airq.notifications.application.dto.NotificationResponse;
import com.oxaira.airq.notifications.domain.model.Notification;
import com.oxaira.airq.notifications.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateNotificationUseCase {

    private final NotificationRepository notificationRepository;

    public NotificationResponse execute() {

        Notification notification = notificationRepository.generateNotification();

        return new NotificationResponse(
                notification.getTitle(),
                notification.getMessage(),
                notification.getSeverity(),
                notification.getCreatedAt());
    }
}