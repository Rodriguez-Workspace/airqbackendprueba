package com.oxaira.airq.notifications.domain.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationMessageService {

    public String generateMessage(
            String riskLevel) {

        return switch (riskLevel) {

            case "HIGH" ->
                "Critical air quality detected. Immediate action required.";

            case "MEDIUM" ->
                "Moderate air quality detected. Monitoring recommended.";

            default ->
                "Air quality conditions are normal.";
        };
    }
}