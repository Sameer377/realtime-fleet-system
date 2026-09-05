package com.robotsimulator.app;

/**
 * @author Sameer Shaikh
 * @date 04-09-2026
 * @description ${DESCRIPTION}
 *///TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String MQTT_BROKER =
            System.getenv().getOrDefault("MQTT_BROKER", "tcp://localhost:1883");
    private static final String TOPIC = "robot/%s/status";

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java RobotSimulator <robotId>");
            return;
        }

        String robotId = args[0];

        ObjectMapper objectMapper = new ObjectMapper();
        List<RobotEvent> events = new ArrayList<>();

        InputStream inputStream =
                Main.class
                        .getClassLoader()
                        .getResourceAsStream("events.jsonl");

        if (inputStream == null) {
            System.out.println("events.jsonl not found in resources");
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream))) {

            String line;

            while ((line = reader.readLine()) != null) {
                RobotEvent event =
                        objectMapper.readValue(line, RobotEvent.class);

                events.add(event);
            }

        } catch (Exception e) {
            System.err.println("Failed to read events.jsonl: " + e.getMessage());
            return;
        }

        List<RobotEvent> robotEvents =
                events.stream()
                        .filter(event -> event.robot_id().equals(robotId))
                        .toList();

        if (robotEvents.isEmpty()) {
            System.out.println("No events found for " + robotId);
            return;
        }

        System.out.println("Starting simulation for " + robotId);

        MqttClient client;

        try {
            client = new MqttClient(
                    MQTT_BROKER,
                    "simulator-" + robotId
            );
        } catch (Exception e) {
            System.err.println(
                    "Failed to create MQTT client: " + e.getMessage()
            );
            return;
        }

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);

        int maxAttempts = 10;
        int retryDelayMs = 5000;

        boolean connected = false;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            try {

                System.out.println(
                        "Connecting to MQTT... attempt "
                                + attempt + "/" + maxAttempts
                );

                client.connect(options);

                connected = true;

                System.out.println("Connected to MQTT");

                break;

            } catch (Exception e) {

                System.err.println(
                        "MQTT connection failed: " + e.getMessage()
                );

                if (attempt < maxAttempts) {

                    System.out.println(
                            "Retrying in 5 seconds..."
                    );

                    try {
                        Thread.sleep(retryDelayMs);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        System.err.println("Retry interrupted");
                        return;
                    }
                }
            }
        }

        if (!connected) {
            System.err.println(
                    "Failed to connect to MQTT after "
                            + maxAttempts
                            + " attempts. Stopping robot."
            );
            return;
        }

        for (RobotEvent event : robotEvents) {

            try {

                String topic =
                        String.format(TOPIC, robotId);

                String payload =
                        objectMapper.writeValueAsString(event);

                MqttMessage message =
                        new MqttMessage(payload.getBytes());

                message.setQos(1);

                client.publish(topic, message);

                System.out.println(
                        "Published: " + payload
                );

                Thread.sleep(5000);

            } catch (Exception e) {

                System.err.println(
                        "Failed to publish event: "
                                + e.getMessage()
                );

                break;
            }
        }

        try {
            if (client.isConnected()) {
                client.disconnect();
            }
            client.close();
        } catch (Exception e) {
            System.err.println(
                    "Error while closing MQTT client: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "Simulation completed for " + robotId
        );
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