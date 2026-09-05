1. How would you add a new feature?
- The system separates MQTT ingestion ,fleet state, persistence, and APIs, so new features can be added without changing the robot communication.
- For example : a low-battery alert or disconnect alert could be added after telemetry is processed. 
- It could detect low battery or disconnected robot and send an alert through WebSocket.

2. What happens with 500 robots?
- The first limitation would be websocket fanout and database writes if telemetry frequency increases significantly.
- I would scale the backend horizontally and move shared fleet state to Redis so multiple backend instances can access the same state.

3. What if bandwidth is limited?
- I have doubt with this
- right now i can say i would use compact encoding to send status or adjust status frequency

4. What if a robot goes down?
- The backend can track when each robot last sent telemetry. 
- If no update is received for a configured period (that is 15 sec), the robot can be marked as offline.
- i  have used scheduler for this which will check it after every 5 sec.

5. What if updates are slow or arrive out of order?
- If updates stop arriving the system keeps the last known state and can mark the robot as offline. 
- Once the robot reconnects, new telemetry updates its state again.
- and for ordering if the current state time is greater than prev one then only we will update the state so any unordered event will not be accepted at this setup.

-- answered by sameer shaikh