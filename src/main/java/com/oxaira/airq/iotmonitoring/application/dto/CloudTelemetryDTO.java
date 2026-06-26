package com.oxaira.airq.iotmonitoring.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloudTelemetryDTO {

    @JsonProperty("hardware_mac_id")
    private String hardwareMacId;

    @JsonProperty("metrics")
    private Metrics metrics;

    @JsonProperty("edge_processed_at")
    private String edgeProcessedAt;

    @JsonProperty("local_alert_status")
    private String localAlertStatus;

    @Data
    public static class Metrics {
        private Double co2;
        private Double pm25;
        private Double temperature;
        private Double humidity;
    }
}
