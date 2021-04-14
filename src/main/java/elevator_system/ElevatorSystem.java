package elevator_system;

import users.Person;

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

    public List<List<Person>> waitingPeople;
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
        waitingPeople = new ArrayList<>(maxFloor - minFloor + 1);
        for(int i = 0; i < maxFloor - minFloor + 1; ++i) {
            waitingPeople.add(i, new LinkedList<>());
        }
    }

    public void pickup(Person person) {
        // I pick the elevator with the best (the lowest metric value)
        Elevator bestElevator = elevators.stream().min(Comparator.comparingInt(e -> getElevatorMetric(e, person.from, person.dir))).get();

        bestElevator.pickup(person.from);
        waitingPeople.get(person.from - minFloor).add(person);
    }

    private int getElevatorMetric(Elevator elevator, int floor, ElevatorDirection dir) {
        // Best elevators are:
        // 1) Already moving to the same floor in the same direction (but that would take more time to count (iterating over array inside elevator))
        // 2) Idle on the same floor
        // TODO: Reconsider this stuff
        // 3) Moving in the same direction with and being able
        // 4) Moving in the same direction with
        int metric;
        if(elevator.dir == ElevatorDirection.IDLE) {
            metric = Math.abs(elevator.currentFloor - floor);
        }// I'm gonna assume that elevator needs at least one floor to slow down, so if one is on the same floor the person is but is moving, it will not be picked up primarily
        else if(elevator.dir == dir && (elevator.currentFloor - floor) * elevator.dir.dirVal() < 0 ) {
            // It could also calculate other floors that the elevator has to stop for and what not but I the code would look awful
            // I assume that elevator going in the same direction is better than the one that is IDLE
            metric = Math.abs(elevator.currentFloor - floor) - 1;
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
            if(elevator.floorButtons[elevator.currentFloor - minFloor]) {
                if (elevator.currentFloor == elevator.lastFloorInDir) {
                    elevator.dir = ElevatorDirection.IDLE;
                }
                // people getting out
                elevator.simGettingOut();
                // people getting in

                Iterator<Person> it = waitingPeople.get(elevator.currentFloor - minFloor).listIterator();
                while(it.hasNext()) {
                    Person person = it.next();
                    if(person.dir == elevator.dir || elevator.dir == ElevatorDirection.IDLE) {
                        elevator.getIn(person);
                        it.remove();
                    }
                }

                elevator.floorButtons[elevator.currentFloor - minFloor] = false;
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
            if (elevator.currentFloor == elevator.lastFloorInDir || (elevator.peopleInside.size() != 0 && elevator.dir == ElevatorDirection.IDLE)) {
                elevator.dir = ElevatorDirection.IDLE;
                elevator.changeDir();
            }
        }
    }
    public List<ElevatorState> status() {
        return elevators.stream().map(e -> new ElevatorState(e.id, e.currentFloor, e.lastFloorInDir)).collect(Collectors.toList());
    }
    public void printSystemStatus() {
        System.out.println("===Elevator System Info===");
        elevators.forEach(Elevator::printInfo);
        System.out.println("==========================");
    }

}
