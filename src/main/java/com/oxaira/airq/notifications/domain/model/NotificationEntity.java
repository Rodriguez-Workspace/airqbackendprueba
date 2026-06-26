package com.oxaira.airq.notifications.domain.model;

import com.oxaira.airq.iam.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // 'AI_ACTION' or 'HARDWARE_FAILURE'

    private String location; // e.g. 'Aula 301'

    @Column(length = 500)
    private String diagnosis;

    @Column(length = 500)
    private String executedAction;

    private Boolean isRead;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;
}
