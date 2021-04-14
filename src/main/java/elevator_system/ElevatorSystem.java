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

        bestElevator.pickup(person.from, person.dir);
        waitingPeople.get(person.from - minFloor).add(person);
    }

    private int getElevatorMetric(Elevator elevator, int floor, ElevatorDirection dir) {
        int metric;
        // Best elevators are:
        if((elevator.dir == dir || elevator.willChangeDir) && elevator.floorButtons[floor - minFloor]) {
            // 1) Already moving to the same floor in the same direction
            metric = -2;
        }
        else if(elevator.dir == dir && (elevator.currentFloor - floor) * elevator.dir.dirVal() < 0 && !elevator.willChangeDir) {
            // 2) Moving in the same direction (second condition is because elevator needs time to slow down)
            metric = Math.abs(elevator.currentFloor - floor) - 1;
        } else if(elevator.dir == ElevatorDirection.IDLE) {
            // 3) Idle on the same floor
            metric = Math.abs(elevator.currentFloor - floor);
        } else if(!elevator.willChangeDir){
            // 4) Going in another direction (Or called when already on the floor but moving so needing time to slow down)
            // we have to keep in mind that elevator route can get longer so it is twice the distance to that direction's end
            if((floor - elevator.lastFloorInDir) * elevator.dir.dirVal() > 0) {
                metric = Math.abs(elevator.currentFloor - floor);
            } else {
                if (elevator.dir == ElevatorDirection.DOWN) {
                    metric = 2 * (elevator.currentFloor - minFloor) + (floor - elevator.currentFloor);
                } else {
                    metric = 2 * (maxFloor - elevator.currentFloor) + (elevator.currentFloor - floor);
                }
            }
        } else {
            // 5) Elevator will change dirs twice so:
            if(elevator.dir == ElevatorDirection.DOWN) {
                metric = 2 * (elevator.currentFloor - minFloor) + (floor - elevator.currentFloor);
            } else {
                metric = 2 * (maxFloor - elevator.currentFloor) + (elevator.currentFloor - floor);
            }
        }
        return metric;
    }

    public void step() {
        // Move down or open (For now I assume constant time for people to get out (although very unrealistic))
        for(Elevator elevator : elevators) {
            // If elevator should open there, it opens
            if(elevator.floorButtons[elevator.currentFloor - minFloor]) {
                if (elevator.currentFloor == elevator.lastFloorInDir) {
                    elevator.dir = ElevatorDirection.IDLE;
                }
                elevator.floorButtons[elevator.currentFloor - minFloor] = false;

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
        System.out.println("===Elevators Info===");
        elevators.forEach(Elevator::printInfo);
        System.out.println("===Waiting People Info===");
        for(int i = waitingPeople.size() - 1; i >= 0; --i) {
            System.out.println("--Floor[" + (i + minFloor) + "]--");
            waitingPeople.get(i).forEach(System.out::println);
        }
        System.out.println("==========================");
    }

}
