package manager;

import elevator_system.ElevatorDirection;
import elevator_system.ElevatorSystem;
import gui.SimulationCanvas;
import users.Person;

import java.util.*;

public class SimulationManager {

    private final ElevatorSystem elSystem;
    private final SimulationCanvas canvas;

    private int step = 0;

    private final List<List<Person>> people = Arrays.asList(
            Arrays.asList(new Person(2, ElevatorDirection.DOWN, -1), new Person(2, ElevatorDirection.UP, 4)),
            Arrays.asList(new Person(0, ElevatorDirection.UP, 6)),
            Collections.emptyList(),
            Collections.emptyList()
    );

    public SimulationManager(ElevatorSystem elSystem, SimulationCanvas canvas) {
        this.elSystem = elSystem;
        this.canvas = canvas;
    }

    public void spawnRandomPerson(int from, ElevatorDirection dir) {
        elSystem.pickup(Person.createRandomDestPerson(from, dir, elSystem.minFloor, elSystem.maxFloor));
        canvas.repaint();
    }

    public void nextStep() {
        /*if(step < people.size())
            people.get(step++).forEach(elSystem::pickup);*/
        elSystem.step();
        canvas.repaint();
    }

    public void quickRestart() {
        step = 0;
    }

    public void printInfo() {
        elSystem.printSystemStatus();
    }
}
