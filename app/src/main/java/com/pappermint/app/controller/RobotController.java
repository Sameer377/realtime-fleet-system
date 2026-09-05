package com.pappermint.app.controller;

import com.pappermint.app.dto.RobotConfigDTO;
import com.pappermint.app.dto.RobotTelemetryDTO;
import com.pappermint.app.entity.Robot;
import com.pappermint.app.entity.RobotTelemetry;
import com.pappermint.app.service.FleetService;
import com.pappermint.app.service.RobotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class RobotController {

    private final RobotService robotService;
    private final FleetService fleetService;

    @GetMapping("/robot/list")
    public ResponseEntity<List<Robot>> getRobotState(
            @RequestParam List<String> listRobots
    ){
        return ResponseEntity.ok(robotService.listRobots(listRobots));
    }

    @PostMapping("/robots")
    public ResponseEntity<List<Robot>> registerRobot(
            @RequestBody List<RobotConfigDTO> robots
    ){
        return ResponseEntity.ok(robotService.registerRobots(robots));
    }

  /*  @PutMapping("/robot/status")
    public ResponseEntity updateCurrentState(
            @RequestBody RobotTelemetryDTO robotTelemetryDTO
    ){
        fleetService.updateState(robotTelemetryDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }*/

    @GetMapping("/robot/{robotId}")
    public RobotTelemetryDTO getRobotState(
            @PathVariable String robotId
    ) {
        return fleetService.getRobotState(robotId);
    }

    @GetMapping("/fleet/status")
    public ResponseEntity<Collection<RobotTelemetryDTO>> getFleetState() {
        return ResponseEntity.ok(fleetService.getFleetState());
    }

    @GetMapping("/robots/history/{robot_id}")
    public ResponseEntity<List<RobotTelemetry>> getRobotHistory(
            @PathVariable(value = "robot_id") String robotId
    ) {
        List<RobotTelemetry> history = fleetService.getRobotHistory(robotId);
        return ResponseEntity.ok(history);
    }

}
