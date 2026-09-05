package com.pappermint.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pappermint.app.entity.Robot;
import lombok.Data;

@Data
public class RobotConfigDTO {

    @JsonProperty("robot_id")
    private String robotId;

    @JsonProperty("robot_type")
    private String robotType;

    private StartDTO start;

    public static Robot toEntity(RobotConfigDTO dto) {
        return Robot.builder()
                .robotId(dto.getRobotId())
                .robotType(dto.getRobotType())
                .startX(dto.getStart().getX())
                .startY(dto.getStart().getY())
                .build();
    }

    @Data
    public static class StartDTO {
        private Double x;
        private Double y;
    }
}