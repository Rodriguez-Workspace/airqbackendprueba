package com.oxaira.airq.subscription.domain.model;

import com.oxaira.airq.iam.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String organizationName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    @Enumerated(EnumType.STRING)
    private Plan plan;
    
    // Explicit counter of how many sensors the admin assigned to this subscription.
    // If null or 0, it means it's not strictly overriding the DB count, 
    // but useful if we want to bill by "assigned sensors" instead of "active physical sensors".
    private Integer assignedSensorsCount; 

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}