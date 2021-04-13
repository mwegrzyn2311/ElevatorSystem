package gui;

import elevator_system.ElevatorSystem;
import manager.SimulationManager;

import javax.swing.*;
import java.awt.*;

public class ElevatorsFrame extends JFrame {

    public ElevatorsFrame() {
        super("Elevators");
        this.setLayout(new BorderLayout());

        int elevatorsCount = 4;
        int minFloor = -2;
        int maxFloor = 8;

        ElevatorSystem elSystem = new ElevatorSystem(elevatorsCount, minFloor, maxFloor);
        SimulationCanvas canvas = new SimulationCanvas(elSystem);
        this.add(canvas, BorderLayout.CENTER);

        this.add(new ButtonPanel(new SimulationManager(elSystem, canvas)), BorderLayout.SOUTH);

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }

}
