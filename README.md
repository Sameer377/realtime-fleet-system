# Fleet Management Backend

## 1. How to Run the Project

### Prerequisites

Make sure the following are installed:

- Docker
- Docker Compose
- Git

### Step 1: Clone the Repository

Clone the project repository:

```bash
git clone git@github.com:Sameer377/realtime-fleet-system.git
```

Example:

```bash
git clone git@github.com:Sameer377/realtime-fleet-system.git
```

### Step 2: Navigate to the Project Directory

```bash
cd fleet-management-system
```

For example:

```bash
cd fleet-management-system
```

Make sure the `docker-compose.yml` file is present in the current directory:

```bash
ls
```

The project structure should contain files similar to:

```text
docker-compose.yml
README.md
ANSWERS.md
SYSTEM_DESIGN.md
app/
robot/
```

### Step 3: Start the Application

Start the complete application using Docker Compose:

```bash
docker compose up
```

Docker Compose automatically starts:

- MQTT Broker
- Spring Boot Backend
- 8 Robot Simulators (`r1` to `r8`)

No manual setup of Java, Maven, MQTT, or the robot simulators is required.

### Step 4: Run in Detached Mode

To start the application in the background:

```bash
docker compose up -d
```

### Step 5: Check Running Containers

```bash
docker compose ps
```

This shows the status of the backend, MQTT broker, and robot simulator containers.

### Step 6: View Logs

To view logs from all services:

```bash
docker compose logs -f
```

To view only the backend logs:

```bash
docker compose logs -f backend
```

### Step 7: Stop the Application

```bash
docker compose down
```

This stops and removes the containers created by Docker Compose.

---

## 2. Architecture Overview

The system consists of:

- **8 Robot Simulators** (`r1` to `r8`) that replay robot events and publish telemetry.
- **MQTT Broker** using Eclipse Mosquitto for communication between robots and the backend.
- **Spring Boot Backend** that consumes robot telemetry, maintains the current fleet state, stores robot history, and exposes REST and WebSocket APIs.

### Data Flow

```text
Robot r1 -+
Robot r2 -+
Robot r3 -+
Robot r4 -+
Robot r5 -+--> MQTT Broker --> Spring Boot Backend
Robot r6 -+                         |
Robot r7 -+                         +--> REST API
Robot r8 -+                         |
                                     +--> WebSocket
```

Docker Compose runs each robot as a separate service, along with the MQTT broker and backend.

### Main Components

```text
Robot Simulators
       |
       | MQTT
       v
 MQTT Broker
       |
       v
Spring Boot Backend
       |
       +------> Fleet State
       |
       +------> Robot History
       |
       +------> REST API
       |
       +------> WebSocket
```

The backend receives telemetry from the robots through MQTT. It maintains the latest state of each robot and makes that state available through the REST API and WebSocket connection.

---

## 3. API / WebSocket Endpoints

### WebSocket

Real-time robot updates:

```text
ws://localhost:8080/ws/robots
```

Clients can connect to this endpoint to receive real-time fleet updates.

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
- Troubleshooting Docker, MQTT, and Spring Boot configuration.
- Reviewing edge cases and improving implementation clarity.

-- Created by Sameer Shaikh