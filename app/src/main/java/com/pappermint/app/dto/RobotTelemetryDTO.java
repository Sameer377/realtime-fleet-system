package com.pappermint.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotTelemetryDTO {
    @JsonProperty("robot_id")
    private String robotId;
    private Double x;
    private Double y;
    private Double battery;
    private String status;
    private Long t;

    @JsonIgnore
    private Instant lastSeen;
}