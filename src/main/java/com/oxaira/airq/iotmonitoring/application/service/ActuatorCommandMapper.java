package com.oxaira.airq.iotmonitoring.application.service;

import com.oxaira.airq.iotmonitoring.application.dto.ActuatorCommand;
import org.springframework.stereotype.Component;

@Component
public class ActuatorCommandMapper {

    public ActuatorCommand mapAiActionToCommand(String aiAction) {
        if (aiAction == null) {
            aiAction = "";
        }
        
        String upperAction = aiAction.toUpperCase();
        
        // Reglas de Mapeo
        boolean extractor = upperAction.contains("EXTRACTOR") || upperAction.contains("VENTILACIÓN");
        boolean hepa = upperAction.contains("HEPA") || upperAction.contains("FILTRO");
        boolean acCool = upperAction.contains("COOL");
        boolean acDry = upperAction.contains("DRY");
        
        // Estado de Rejillas: Por defecto están abiertas para ventilación.
        // Si el AI dice que se cierren por toxicidad o aislamiento, se marcan como cerradas (false).
        boolean dampersOpen = !upperAction.contains("CERRADAS") && !upperAction.contains("AISLAMIENTO");

        return ActuatorCommand.builder()
                .extractor(extractor)
                .hepa(hepa)
                .ac_cool(acCool)
                .ac_dry(acDry)
                .dampers_open(dampersOpen)
                .build();
    }
}
