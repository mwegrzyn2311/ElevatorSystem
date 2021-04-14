# ElevatorSystem

### Running the project

Project was written in Java 11

Project is made using maven so it can be easily imported.

It can also be run from the command line using prepared jar files like that:
```
java -jar jars/Elevators-1.0.jar [elevators count] [min floor] [max floor]
```

### Algorithm

The algorithm used for the elevator system works like that:
1. When a person clicks an elevator button (either up or down), all elevators are checked and the best one is assigned to go to that floor. Best elevator is picked based on the metric which currently tries to choose elevator going in the same direction or idle elevator that is the closest to that floor. If no such elevator is found then elevator that goes in another direction but will soon change its direction (meaning it is either close to the top going up or close to the bottom going down) is chosen
2. Each elevator will be going in the same direction until there won't be any more floors in this direction that people inside want to get to or people there are already assigned to this elevator.
3. When elevator stops going in the same direction, it changes it directions if it should or goes back to idle state

### Simulation

<img src="./readme-images/elevatorsSim.PNG?raw=true" alt="Alt text" style="zoom:50%;" />

1. Lobby - people inside are people who already called an elevator and are waiting for it to arrive; in the top-right corner floor number is displayed
2. Elevator shaft - Elevators move only vertically and one vertical line contains one elevator
3. Elevator - Elevator moving in the corresponding shaft.
4. Elevator buttons - By clicking up or down buttons, a person is spawned so that it simulates a new person willing to use the elevator arriving
5. Next step button - Clicking this button causes one simulation step to be performed
6. Restart button - Restarts the simulation
7. Info button - Displays info about current elevators state in the console.
