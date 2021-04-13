package elevator_system;

public enum ElevatorDirection {
    IDLE,
    UP,
    DOWN;

    public int dirVal() {
        switch(this) {
            case DOWN:
                return -1;
            case UP:
                return 1;
            case IDLE:
                return 0;
        }
        return 0;
    }

    public static ElevatorDirection fromFloorDiff(int floorDiff) {
        if(floorDiff < 0) {
            return UP;
        } else if (floorDiff > 0) {
            return DOWN;
        } else {
            return IDLE;
        }
    }

    @Override
    public String toString() {
        switch(this) {
            case DOWN:
                return "DOWN";
            case UP:
                return "UP";
            case IDLE:
                return "IDLE";
            default:
                return "UnhandledDir";
        }
    }
}
