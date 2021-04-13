# ElevatorSystem

### Running the project

Project was written in Java 11

Project is made using maven so it can be easily imported.

It can also be run from the command line using prepared jar files like that:
```
java -jar jars/Elevators-1.0.jar
```

### Algorithm

The algorithm used for the elevator system works like that:
1. When a person clicks an elevator button (either up or down), all elevators are checked and the best one is assigned to go to that floor. Best elevator is picked based on the metric which currently tries to choose elevator going in the same direction or idle elevator that is the closest to that floor. If no such elevator is found then elevator that goes in another direction but will soon change its direction (meaning it is either close to the top going up or close to the bottom going down) is chosen
2. Each elevator will be going in the same direction until there won't be any more floors in this direction that people inside want to get to or people there are already assigned to this elevator.
3. When elevator stops going in the same direction, it changes it directions if it should or goes back to idle state

### Simulation

Currently simulation is based on hardcoded data but I'll try to add either a few data sets to choose from for the simulation or buttons for the user to manually call elevators and experiment (or both)
