package com.oxaira.airq.iotmonitoring.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double co2;

    private Double pm25;

    private Double temperature;

    private Double humidity;

    private LocalDateTime recordedAt;

    @ManyToOne
    private Sensor sensor;
}