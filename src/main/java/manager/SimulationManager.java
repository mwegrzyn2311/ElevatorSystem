package manager;

import elevator_system.Elevator;
import elevator_system.ElevatorDirection;
import elevator_system.ElevatorSystem;
import gui.SimulationCanvas;
import users.Person;

import java.util.*;

public class SimulationManager {

    private final ElevatorSystem elSystem;
    private final SimulationCanvas canvas;

    public SimulationManager(ElevatorSystem elSystem, SimulationCanvas canvas) {
        this.elSystem = elSystem;
        this.canvas = canvas;
    }

    public void spawnRandomPerson(int from, ElevatorDirection dir) {
        elSystem.pickup(Person.createRandomDestPerson(from, dir, elSystem.minFloor, elSystem.maxFloor));
        canvas.repaint();
    }

    public void nextStep() {
        elSystem.step();
        canvas.repaint();
    }

    public void hardRestart() {
        elSystem.waitingPeople.forEach(List::clear);
        elSystem.elevators.forEach(Elevator::reset);
        canvas.repaint();
    }

    public void printInfo() {
        elSystem.printSystemStatus();
    }
}
