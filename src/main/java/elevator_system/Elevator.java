package elevator_system;

import java.util.*;

public class Elevator {
    public final int id;
    public final int minFloor;
    public final int maxFloor;
    public int currentFloor;
    public ElevatorDirection dir = ElevatorDirection.IDLE;
    public int lastFloorInDir;

    public boolean[] floorButtons;
    public List<Integer> floorsToVisitAfterDirChange = new LinkedList<>();

    public Elevator(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.currentFloor = minFloor;
        this.lastFloorInDir = minFloor;
        // We don't have to init cause boolean defaults to false in Java
        floorButtons = new boolean[maxFloor + minFloor + 1];

    }

    public void pickup(int floor) {
        switch(dir) {
            case IDLE:
                this.dir = ElevatorDirection.fromFloorDiff(currentFloor - floor);
                floorButtons[floor] = true;
                lastFloorInDir = floor;
                break;
            case DOWN:
                if(floor >= currentFloor) {
                    floorsToVisitAfterDirChange.add(floor);
                } else {
                    floorButtons[floor] = true;
                    if(floor < lastFloorInDir)
                        lastFloorInDir = floor;
                }
                break;
            case UP:
                if(floor <= currentFloor) {
                    floorsToVisitAfterDirChange.add(floor);
                } else {
                    floorButtons[floor] = true;
                    if(floor > lastFloorInDir)
                        lastFloorInDir = floor;
                }
                break;
        }
    }

    /** Changes dir if needed, otherwise elevator goes to IDLE state */
    public void changeDir() {
        floorsToVisitAfterDirChange.forEach(floor -> floorButtons[floor] = true);
        floorsToVisitAfterDirChange.clear();

        switch (dir) {
            case UP:
                dir = ElevatorDirection.IDLE;
                for(int i = currentFloor - minFloor - 1; i >= 0; --i) {
                    if(floorButtons[i]) {
                        lastFloorInDir = i;
                        dir = ElevatorDirection.DOWN;
                    }
                }
            case DOWN:
                dir = ElevatorDirection.IDLE;
                for(int i = currentFloor - minFloor; i <= maxFloor - minFloor; ++i) {
                    if(floorButtons[i]) {
                        lastFloorInDir = i;
                        dir = ElevatorDirection.UP;
                    }
                }
        }

    }
}
