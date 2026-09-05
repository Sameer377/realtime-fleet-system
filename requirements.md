Functional Requirement 
- robots should be able to send updates frequently to the server
- ingest updates from all robots
- process every robot update
- server should persist telemetry changes into the DB to track the robot history
- Monitoring user can see snapshot of fleet state through rest api and realtime updates through websocket

Non functional 
- reliablity : system should work when robot is disconnected, similarly for websocket connection 
- consistency : current fleet state by websocket and rest api should be same
- fault tolerance : in case of message miss order it should process it as per the order

Required entities
- robots
- state

Apis
- api/robots GET
- api/robots/history/{robot_id} GET

