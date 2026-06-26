package com.oxaira.airq.iotmonitoring.application.dto;

import java.time.LocalDateTime;

public record SensorStatusDTO(
    Long id,
    String serialNumber,
    String campus,
    String location,
    boolean isOnline,
    Double latestCo2,
    Double latestPm25,
    LocalDateTime lastSeen
) {}
