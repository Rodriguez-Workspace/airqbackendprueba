package com.oxaira.airq.analytics.infrastructure.adapter;

import org.springframework.stereotype.Component;

@Component
public class AnalyticsNotificationAdapter {

    public void notifyPoorAirQuality() {

        System.out.println(
                "Air quality alert generated");

    }
}