Welcome to Team 0115's Toy Robot World, here's some information you might be needing before you start:

Robot Commands:
1. Launch command- enter a robot type (e.g. sniper, trooper, miner) and robot name to launch a new robot. e.g. launch sniper HAL.
2. Forward command- move your robot forward a certain number of steps e.g. forward 15.
3. Back command- move your robot backwards a certain number of steps e.g. back 15
4. Left/Right command- turn your robot to the left or right by a full 90 degrees.
5. Look command- Robot looks in cardinal directions for obstacles and other robots within the world.
6. Repair command- Repairs robot shields over a set amount of time, during which the robot cannot do anything else.
7. Fire/Shoot command- robot fires a bullet over a set distance, amount of bullets and distance is decided by robot type (Miner robots cannot fire/shoot).
8. Mine command- robot sets down a mine over a period of time that kills robots which step on it instantly and then moves forward 1 step (Sniper/Trooper robots
can't set mines).
10. State command- returns information about your robot (direction, position, kind, name, shield, bullets (if applicable)).

Starting Toy Robot World (and putting a robot inside the world):
1. start MultiServer.java
2. start Client.java
3. In the Client.java instance, use the launch command (nothing else can be done until this is completed)

Obstacle types:
1. Walls- non-lethal walls which block your robot from moving through them.
2. Pits- lethal areas in which your robot will die instantly from entering (or attempting to pass through).
3. Robots- other robots are 'obstacles' too, treated in a manner similar to walls.
4. Mines- dropped by Miner type robots, these, like pits, instantly kill robots which step on them..

Robot types:
1. Sniper- Long range robot, holds one bullet which can be fired over a range of five steps in any cardinal direction.
2. Trooper- Close range robot, holds five bullets which can be fired directly in front of the robot (one step forward).
3. Miner- Trap setting robot, robot can set mines down to damage robots that walk on them.

Advice:
1. Use the look command frequently
2. move cautiously, entering 'forward 50' and falling into a pit at step 10 will ruin your game for obvious reasons.
3. Do not place mines while an obstacle is one step in front of your robot, your robot will panic and bomb itself.
4. Keep in mind reload/setmine/repair times, as you are vulnerable while doing these things.
