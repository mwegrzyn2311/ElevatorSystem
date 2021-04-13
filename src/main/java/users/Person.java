package users;

import elevator_system.ElevatorDirection;

public class Person {
    public int from;
    // I keep so that person can be a troll going down but clicking up
    public ElevatorDirection dir;
    public int to;

    public Person(int from, ElevatorDirection dir, int to) {
        this.from = from;
        this.dir = dir;
        this.to = to;
    }

    public static Person createRandomDestPerson(int from, ElevatorDirection dir, int minFloor, int maxFloor) {
        int dest;
        int floorCount = maxFloor - minFloor + 1;
        do {
            dest = (int)(Math.random()*floorCount) + minFloor;
        } while(dest == from);
        return new Person(from, dir, dest);
    }
}
