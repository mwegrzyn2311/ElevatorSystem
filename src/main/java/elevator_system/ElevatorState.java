package elevator_system;

public class ElevatorState {
    public int id;
    public int currentFloor;
    public int destinationFloor;

    public ElevatorState(int id, int currentFloor, int destinationFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.destinationFloor = destinationFloor;
    }
}
