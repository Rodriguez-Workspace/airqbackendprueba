package com.oxaira.airq.notifications.application.dto;

import com.oxaira.airq.notifications.domain.model.NotificationEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationEntityDTO {
    private Long id;
    private String type;
    private String location;
    private String diagnosis;
    private String executedAction;
    private Boolean isRead;
    private String createdAt;

    public static NotificationEntityDTO fromEntity(NotificationEntity entity) {
        NotificationEntityDTO dto = new NotificationEntityDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setLocation(entity.getLocation());
        dto.setDiagnosis(entity.getDiagnosis());
        dto.setExecutedAction(entity.getExecutedAction());
        dto.setIsRead(entity.getIsRead());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() + "Z" : null);
        return dto;
    }
}
