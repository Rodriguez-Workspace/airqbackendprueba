package com.oxaira.airq.notifications.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    private String title;

    private String message;

    private String severity;

    private LocalDateTime createdAt;
}