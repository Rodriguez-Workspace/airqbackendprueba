package com.oxaira.airq.iotmonitoring.infrastructure.adapter;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface ActuatorCommandProducer {
    void sendCommand(String commandPayload);
}
