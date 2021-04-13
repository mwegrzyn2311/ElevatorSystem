package manager;

import elevator_system.ElevatorDirection;
import elevator_system.ElevatorSystem;
import gui.SimulationCanvas;

import java.util.*;

public class SimulationManager {

    private final ElevatorSystem elSystem;
    private final SimulationCanvas canvas;

    private int step = 0;

    private final List<List<SimStep>> simSteps = Arrays.asList(
            Arrays.asList(new SimStep(2, ElevatorDirection.DOWN, 1), new SimStep(2, ElevatorDirection.UP, 3)),
            Arrays.asList(new SimStep(0, ElevatorDirection.UP, 4)),
            Collections.emptyList(),
            Collections.emptyList()
    );

    public SimulationManager(ElevatorSystem elSystem, SimulationCanvas canvas) {
        this.elSystem = elSystem;
        this.canvas = canvas;
    }

    public void nextStep() {
        if(step < simSteps.size())
            simSteps.get(step++).forEach(simStep -> elSystem.pickupAndChooseDestination(simStep.from, simStep.dir, simStep.to));
        elSystem.step();
        canvas.repaint();
    }
}
