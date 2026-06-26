package com.oxaira.airq.iotmonitoring.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActuatorCommand {
    private boolean extractor;
    private boolean hepa;
    private boolean ac_cool;
    private boolean ac_dry;
    private boolean dampers_open;
}
