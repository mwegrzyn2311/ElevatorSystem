package manager;

import elevator_system.ElevatorDirection;

public class SimStep {
    public int from;
    public ElevatorDirection dir;
    public int to;

    public SimStep(int from, ElevatorDirection dir, int to) {
        this.from = from;
        this.dir = dir;
        this.to = to;
    }
}
