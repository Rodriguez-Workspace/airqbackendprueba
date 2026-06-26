package com.oxaira.airq.notifications.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationAdapter {

    public void sendEmail(
            String title,
            String message) {

        System.out.println(
                "Email sent: " + title);
    }
}