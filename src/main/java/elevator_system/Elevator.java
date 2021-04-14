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
    public List<Integer> visitAfterDirChange = new LinkedList<>();
    public boolean willChangeDir = false;

    public boolean[] floorButtons;
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

    public void pickup(int floor, ElevatorDirection personDir) {
        // Last condition is to eliminate case where el from 0 goes to 5 to pickup person wanting to go down but is not assigned to anything else
        if((this.dir != ElevatorDirection.IDLE && this.dir != personDir && (lastFloorInDir - floor) * dir.dirVal() > 0) || (willChangeDir && (lastFloorInDir - floor) * dir.dirVal() < 0)) {
            visitAfterDirChange.add(floor);
        }
        switch (this.dir) {
            case IDLE:
                this.dir = ElevatorDirection.fromFloorDiff(currentFloor - floor);
                floorButtons[floor - minFloor] = true;
                lastFloorInDir = floor;
                break;
            case DOWN:
                floorButtons[floor - minFloor] = true;
                if (floor < lastFloorInDir)
                    lastFloorInDir = floor;
                //}
                break;
            case UP:
                floorButtons[floor - minFloor] = true;
                if (floor > lastFloorInDir)
                    lastFloorInDir = floor;
                break;
        }
        if(this.dir != personDir) {
            willChangeDir = true;
        }
    }

    public void pickFloorWithButton(int to) {
        floorButtons[to - minFloor] = true;
        switch (dir) {
            case IDLE:
                break;
            case DOWN:
            case UP:
                if ((to - lastFloorInDir) * dir.dirVal() > 0) {
                    lastFloorInDir = to;
                }
                break;
        }
    }

    /**
     * Changes dir if needed, otherwise elevator goes to IDLE state
     */
    public void changeDir() {
        if(!visitAfterDirChange.isEmpty()) {
            System.out.println("CHANGING DIR TO PICKUP someone left behind");
        }
        visitAfterDirChange.forEach(floor -> floorButtons[floor - minFloor] = true);
        visitAfterDirChange.clear();

        switch (dir) {
            case UP:
                dir = ElevatorDirection.IDLE;
                for (int i = currentFloor - minFloor; i >= 0; --i) {
                    if (floorButtons[i]) {
                        lastFloorInDir = i + minFloor;
                        dir = ElevatorDirection.DOWN;
                    }
                }
                break;
            case DOWN:
                dir = ElevatorDirection.IDLE;
                for (int i = currentFloor - minFloor; i <= maxFloor - minFloor; ++i) {
                    if (floorButtons[i]) {
                        lastFloorInDir = i + minFloor;
                        dir = ElevatorDirection.UP;
                    }
                }
                break;
            case IDLE:
                for (int i = currentFloor - minFloor; i <= maxFloor - minFloor; ++i) {
                    if (floorButtons[i]) {
                        lastFloorInDir = i + minFloor;
                        dir = ElevatorDirection.UP;
                    }
                }
                for (int i = currentFloor - minFloor; i >= 0; --i) {
                    if (floorButtons[i]) {
                        lastFloorInDir = i + minFloor;
                        dir = ElevatorDirection.DOWN;
                    }
                }
                break;
        }
        willChangeDir = false;
        switch (dir) {
            case UP:
                for (int i = currentFloor - minFloor; i <= maxFloor - minFloor; ++i) {
                    if (floorButtons[i]) {
                        willChangeDir = true;
                        break;
                    }
                }
                break;
            case DOWN:
                for (int i = currentFloor - minFloor; i >= 0; --i) {
                    if (floorButtons[i]) {
                        willChangeDir = true;
                        break;
                    }
                }
                break;
            // unreachable
            case IDLE:
                break;
        }
    }

    public void simGettingOut() {
        peopleInside.removeAll(peopleInside.stream().filter(person -> person.to == currentFloor).collect(Collectors.toList()));
    }

    public void getIn(Person person) {
        System.out.print("Getting in on " + currentFloor + " floor, wanting to go to " + person.to + " | ");
        peopleInside.add(person);
        if (dir == ElevatorDirection.IDLE)
            dir = person.dir;
        pickFloorWithButton(person.to);
        System.out.println(" Direction is now " + dir);
    }

    public void printInfo() {
        System.out.print("El[" + id + "] is at [" + currentFloor + "] | Dir[" + dir + "] | Floor buttons active are ");
        for (int i = 0; i < floorButtons.length; ++i) {
            if (floorButtons[i])
                System.out.print((i + minFloor) + ", ");
        }
        System.out.println();
    }

    public void reset() {
        this.currentFloor = minFloor;
        this.lastFloorInDir = minFloor;
        this.dir = ElevatorDirection.IDLE;
        this.peopleInside = new LinkedList<>();
        Arrays.fill(floorButtons, false);
    }
}
