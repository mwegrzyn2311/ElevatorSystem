package elevator_system;

import java.util.*;
import java.util.stream.Collectors;

/* TODO-ideas:
 *  A few version of system (One that prioritizes the people awaiting time and the one that prioritizes lowering energy consumption)
 *  People limits would be cool but checking it feels unrealistic
 */

public class ElevatorSystem {
    public final int minFloor;
    public final int maxFloor;
    public final int elevatorsCount;

    //private SortedSet<Elevator> elevators = new TreeSet<>(Comparator.comparingInt(e -> e.currentFloor));
    public List<Elevator> elevators;
    //private List<List<>>

    // Regarding minFloor/maxFloor - here I assumed that there is floor 0 (even though there is no such floor in US). It could be a way to improve this app - allow elevator type choice
    public ElevatorSystem(int elevatorsCount, int minFloor, int maxFloor) {
        this.elevatorsCount = elevatorsCount;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.elevators = new ArrayList<>(elevatorsCount);
        for(int i = 0; i < elevatorsCount; ++i) {
            elevators.add(new Elevator(i, minFloor, maxFloor));
        }
    }

    public Elevator pickup(int floor, ElevatorDirection dir) {
        // Best elevators are:
        // 1) Already moving to the same floor in the same direction (but that would take more time to count (iterating over array inside elevator))
        // 2) Idle on the same floor
        // TODO: Reconsider this stuff
        // 3) Moving in the same direction with and being able
        // 4) Moving in the same direction with

        // Find the most suitable elevator
        Elevator bestElevator = elevators.stream().min(Comparator.comparingInt(e -> getElevatorMetric(e, floor, dir))).get();

        bestElevator.pickup(floor);
        return bestElevator;
    }
    public void pickupAndChooseDestination(int floor, ElevatorDirection dir, int destination) {
        Elevator el = pickup(floor, dir);
        el.pickFloorWithButton(destination);
    }

    private int getElevatorMetric(Elevator elevator, int floor, ElevatorDirection dir) {
        int metric;
        if(elevator.dir == ElevatorDirection.IDLE) {
            metric = Math.abs(elevator.currentFloor - floor);
        } // I'm gonna assume that elevator needs at least one floor to slow down, so if one is on the same floor the person is but is moving, it will not be picked up primarily
        else if(elevator.dir == dir && (elevator.currentFloor - floor) * elevator.dir.dirVal() > 0 ) {
            // It could also calculate other floors that the elevator has to stop for and what not but I the code would look awful
            metric = Math.abs(elevator.currentFloor - floor);
        } else {
            // we have to keep in mind that elevator route can get longer so it is twice the distance to that direction's end
            if(elevator.dir == ElevatorDirection.DOWN) {
                metric = 2 * (elevator.currentFloor - minFloor);
            } else {
                metric = 2 * (maxFloor - elevator.currentFloor);
            }
        }
        return metric;
    }

    // TODO: Consider getting rid of it as this implementation doesn't need it and I don't feel like we need teleportating elevators
    public void update() {

    }
    public void step() {
        // Move down or open (For now I assume constant time for people to get out (although very unrealistic))
        for(Elevator elevator : elevators) {
            // If elevator should open there, it opens
            if(elevator.floorButtons[elevator.currentFloor]) {
                // TODO: Simulate people getting out?
                elevator.floorButtons[elevator.currentFloor] = false;
            } else {
                switch (elevator.dir) {
                    case UP:
                        ++elevator.currentFloor;
                        break;
                    case DOWN:
                        --elevator.currentFloor;
                        break;
                    case IDLE:
                    default:
                        break;
                }
            }
        }
        // If job is done, change dir to the other one or to idle and update lastFloorInDir
        for(Elevator elevator : elevators) {
            if (elevator.currentFloor == elevator.lastFloorInDir) {
                elevator.changeDir();
            }
        }
    }
    public List<ElevatorState> status() {
        return elevators.stream().map(e -> new ElevatorState(e.id, e.currentFloor, e.lastFloorInDir)).collect(Collectors.toList());
    }

}
