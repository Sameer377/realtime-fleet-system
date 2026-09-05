package com.pappermint.app.entity;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "robots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Robot {

    @Id
    @Column(name = "robot_id")
    private String robotId;

    @Column(name = "robot_type", nullable = false)
    private String robotType;

    @Column(name = "start_x", nullable = false)
    private Double startX;

    @Column(name = "start_y", nullable = false)
    private Double startY;
}
