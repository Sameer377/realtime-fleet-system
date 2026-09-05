package com.pappermint.app.entity;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "robot_telemetry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "robot_id", nullable = false)
    private String robotId;

    @Column(name = "t", nullable = false)
    private Long t;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    private Double battery;

    @Column(nullable = false)
    private String status;
}
