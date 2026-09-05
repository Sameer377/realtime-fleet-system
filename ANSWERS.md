1. What holds the fleet's current state?
- In FleetService i used ConcurrentHashMap to track current state of fleet and to store history i used H2 in memory database(SQL db)
- This gives both REST and WebSocket access to the same current state. 
- But if we need to centralize this in future then we can use redis server and PostgreSQL

2. What tradeoff did you make?
- Right now the only functional tradeoff is when robot disconnects and reconnects itself but the robot restart it self instead of resume then this is not tracked 
- Because ive considered current sec(T) > prev sec(T) so if robot restarts from zero then it will not track as the condition will be 60T > 5T
- but we are tracking if the robot is disconnected by checking lastseen

3. What did you leave out?
- Not tracking the robot after it reconnected cause the time is restart from 0. But tracking if the time is resumed from where it started.
- I focused on the core requirements and kept the system simple. and ive added all the features mentioned in challenge 
- specfically Pub/Sub model and websocket and restapi 
- i havent added unit test cases


