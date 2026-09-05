package com.pappermint.app.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.pappermint.app.dto.RobotTelemetryDTO;
import com.pappermint.app.service.FleetService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Component
public class MqttConsumer {

    private final ObjectMapper objectMapper;
    private final FleetService fleetService;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void consume(Message<?> message) {
        log.info("Received: {}", message.getPayload());

        String json = message.getPayload().toString();

        RobotEvent event =
                objectMapper.readValue(json, RobotEvent.class);

        fleetService.updateState(RobotTelemetryDTO.builder()
                        .battery(event.battery())
                        .robotId(event.robot_id())
                        .status(event.status())
                        .x(event.x())
                        .y(event.y())
                        .t(event.t())
                        .lastSeen(Instant.now())
                .build());

        log.info("Robot: {}", event.robot_id());
        log.info("X: {}", event.x());
        log.info("Y: {}", event.y());
        log.info("Battery: {}", event.battery());
    }

    public record RobotEvent(
            long t,
            String robot_id,
            double x,
            double y,
            String status,
            double battery,
            JsonNode task_event
    ) {
    }
}