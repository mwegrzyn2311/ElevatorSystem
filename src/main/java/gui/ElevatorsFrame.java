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

        SimulationManager simManager = new SimulationManager(elSystem, canvas);
        this.add(new ButtonPanel(simManager), BorderLayout.PAGE_END);
        this.add(new CallElevatorButtonsPanel(simManager, minFloor, maxFloor), BorderLayout.LINE_START);

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

    }

}
