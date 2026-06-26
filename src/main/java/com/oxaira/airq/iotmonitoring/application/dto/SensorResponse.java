package com.oxaira.airq.iotmonitoring.application.dto;

public record SensorResponse(

        Long id,

        String serialNumber,

        String location,

        Boolean active

) {
}