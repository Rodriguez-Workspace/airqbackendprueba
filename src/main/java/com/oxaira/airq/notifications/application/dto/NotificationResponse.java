package com.oxaira.airq.notifications.application.dto;

import java.time.LocalDateTime;

public record NotificationResponse(

        String title,

        String message,

        String severity,

        LocalDateTime createdAt

) {
}