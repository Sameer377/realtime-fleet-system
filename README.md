# Fleet Management Backend

## 1. How to Run the Project

### Prerequisites

- Docker
- Docker Compose

Start the complete application:

```bash
docker compose up
```

To run in detached mode:

```bash
docker compose up -d
```

To stop the application:

```bash
docker compose down
```

The application starts the backend, MQTT broker, and all 8 robot simulators automatically.

---

## 2. Architecture Overview

The system consists of:

- **8 Robot Simulators** (`r1` to `r8`) that replay robot events and publish telemetry.
- **MQTT Broker** using Eclipse Mosquitto for communication between robots and the backend.
- **Spring Boot Backend** that consumes robot telemetry, maintains the current fleet state, stores robot history, and exposes REST and WebSocket APIs.

### Data Flow

```text
Robot r1 ─┐
Robot r2 ─┤
Robot r3 ─┤
Robot r4 ─┤
Robot r5 ─┼──> MQTT Broker ──> Spring Boot Backend
Robot r6 ─┤                         │
Robot r7 ─┤                         ├──> REST API
Robot r8 ─┘                         └──> WebSocket
```

Docker Compose runs each robot as a separate service, along with the MQTT broker and backend.

---

## 3. API / WebSocket Endpoints

### WebSocket

Real-time robot updates:

```text
ws://localhost:8080/ws/robots
```

### Current Fleet Status

```http
GET http://localhost:8080/fleet/status
```

Returns the current state of the fleet.

### Robot History

```http
GET http://localhost:8080/robots/history/{robot_id}
```

Example:

```text
http://localhost:8080/robots/history/r1
```

Returns the telemetry history for the specified robot.

---

## 4. AI Assistance / Delegation Notes

AI assistance was used during development for:

- Reviewing and debugging implementation issues.
- Discussing architecture and design decisions.
- Improving documentation and README structure.
- Troubleshooting Docker, MQTT, and Spring Boot configuration.

All final implementation decisions, code integration, testing, and project configuration were performed and verified as part of the project development.
