package com.pappermint.app.service;

/**
 * @author Sameer Shaikh
 * @date 03-09-2026
 * @description
 */

import com.pappermint.app.dto.RobotTelemetryDTO;
import com.pappermint.app.entity.RobotTelemetry;
import com.pappermint.app.handler.RobotWebSocketHandler;
import com.pappermint.app.repository.RobotTelemeteryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class FleetService {

    private final RobotTelemeteryRepository robotTelemeteryRepository;
    private final RobotWebSocketHandler robotWebSocketHandler;

    private final ConcurrentHashMap<String, RobotTelemetryDTO> currentState =
            new ConcurrentHashMap<>();

    public void updateState(RobotTelemetryDTO telemetry) {

        RobotTelemetry entity = RobotTelemetry.builder()
                .robotId(telemetry.getRobotId())
                .t(telemetry.getT())
                .x(telemetry.getX())
                .y(telemetry.getY())
                .battery(telemetry.getBattery())
                .status(telemetry.getStatus())
                .build();

        robotTelemeteryRepository.save(entity);

        currentState.compute(
                telemetry.getRobotId(),
                (robotId, existingState) -> {

                    if (existingState == null ||
                            telemetry.getT() > existingState.getT()) {

                        return RobotTelemetryDTO.builder()
                                .robotId(telemetry.getRobotId())
                                .x(telemetry.getX())
                                .y(telemetry.getY())
                                .battery(telemetry.getBattery())
                                .status(telemetry.getStatus())
                                .t(telemetry.getT())
                                .lastSeen(telemetry.getLastSeen())
                                .build();
                    }

                    return existingState;
                }
        );

        robotWebSocketHandler.broadcast(telemetry);

    }

    public RobotTelemetryDTO getRobotState(String robotId) {
        return currentState.get(robotId);
    }

    public Collection<RobotTelemetryDTO> getFleetState() {
        return currentState.values();
    }

    public List<RobotTelemetry> getRobotHistory(String robotId) {
        return robotTelemeteryRepository
                .findByRobotId(robotId);
    }

    @Scheduled(fixedRate = 5000)
    public void checkRobotConnections() {

        log.info("scheduler invoked");
        Instant now = Instant.now();

        currentState.values().forEach(robot -> {

            if (robot.getLastSeen() == null) {
                log.info("last seen is null");
                return;
            }

            long secondsSinceLastSeen =
                    Duration.between(robot.getLastSeen(), now).getSeconds();
            log.info("secondsSinceLastSeen : {}",secondsSinceLastSeen);
            if (secondsSinceLastSeen > 15 && !"offline".equals(robot.getStatus())) {
                robot.setStatus("offline");
                log.info("Robot {} marked offline", robot.getRobotId());

                robotWebSocketHandler.broadcast(robot);
            }
        });
    }
}
