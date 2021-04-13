package elevator_system;

import users.Person;

import java.util.*;
import java.util.stream.Collectors;

public class Elevator {
    public final int id;
    public final int minFloor;
    public final int maxFloor;
    public int currentFloor;
    public ElevatorDirection dir = ElevatorDirection.IDLE;
    public int lastFloorInDir;

    public boolean[] floorButtons;
    public List<Integer> floorsToVisitAfterDirChange = new LinkedList<>();
    public List<Person> peopleInside = new LinkedList<>();

    public Elevator(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.currentFloor = minFloor;
        this.lastFloorInDir = minFloor;
        // We don't have to init cause boolean defaults to false in Java
        floorButtons = new boolean[maxFloor - minFloor + 1];

    }

    public void pickup(int floor) {
        switch(dir) {
            case IDLE:
                this.dir = ElevatorDirection.fromFloorDiff(currentFloor - floor);
                floorButtons[floor - minFloor] = true;
                lastFloorInDir = floor;
                break;
            case DOWN:
                if(floor >= currentFloor) {
                    floorsToVisitAfterDirChange.add(floor);
                } else {
                    floorButtons[floor - minFloor] = true;
                    if(floor < lastFloorInDir)
                        lastFloorInDir = floor;
                }
                break;
            case UP:
                if(floor <= currentFloor) {
                    floorsToVisitAfterDirChange.add(floor);
                } else {
                    floorButtons[floor - minFloor] = true;
                    if(floor > lastFloorInDir)
                        lastFloorInDir = floor;
                }
                break;
        }
    }
    public void pickFloorWithButton(int to) {
        floorButtons[to - minFloor] = true;
        switch(dir) {
            case IDLE:
                dir = ElevatorDirection.fromFloorDiff(currentFloor - to);
                lastFloorInDir = to;
                break;
            case DOWN:
            case UP:
                if((to - lastFloorInDir) * dir.dirVal() > 0 && currentFloor != lastFloorInDir)
                    lastFloorInDir = to;
                break;
        }
    }

    /** Changes dir if needed, otherwise elevator goes to IDLE state */
    public void changeDir() {
        floorsToVisitAfterDirChange.forEach(floor -> floorButtons[floor - minFloor] = true);
        floorsToVisitAfterDirChange.clear();

        switch (dir) {
            case UP:
                dir = ElevatorDirection.IDLE;
                for(int i = currentFloor - minFloor; i >= 0; --i) {
                    if(floorButtons[i]) {
                        lastFloorInDir = i + minFloor;
                        dir = ElevatorDirection.DOWN;
                    }
                }
            case DOWN:
                dir = ElevatorDirection.IDLE;
                for(int i = currentFloor - minFloor; i <= maxFloor - minFloor; ++i) {
                    if(floorButtons[i]) {
                        lastFloorInDir = i + minFloor;
                        dir = ElevatorDirection.UP;
                    }
                }
        }
    }

    public void simGettingOut() {
        peopleInside.removeAll(peopleInside.stream().filter(person -> person.to == currentFloor).collect(Collectors.toList()));
    }

    public void getIn(Person person) {
        System.out.println("Getting in on " + currentFloor + " floor, wanting to go to " + person.to + " | ");
        peopleInside.add(person);
        pickFloorWithButton(person.to);
        System.out.println(" Direction is now " + dir);
    }
}
