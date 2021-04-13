package gui;

import elevator_system.ElevatorDirection;
import manager.SimulationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CallElevatorButtonsPanel extends JPanel {
    private final SimulationManager simManager;

    public CallElevatorButtonsPanel(SimulationManager simManager, int minFloor, int maxFloor) {
        super();
        this.simManager = simManager;
        this.setLayout(new GridLayout((maxFloor - minFloor + 1) * 2, 0));

        try {
            ImageIcon iconUp = new ImageIcon(ImageIO.read(getClass().getResource("/images/arrow-up.png")));
            ImageIcon iconDown = new ImageIcon(ImageIO.read(getClass().getResource("/images/arrow-down.png")));

            for(int i = maxFloor; i >= minFloor; --i) {
                JButton upButton = new JButton();
                upButton.setIcon(iconUp);
                upButton.setPreferredSize(new Dimension(32,38));
                final int floor = i;
                upButton.addActionListener(a -> {
                    simManager.spawnRandomPerson(floor, ElevatorDirection.UP);
                });
                this.add(upButton);

                JButton downButton = new JButton();
                downButton.setIcon(iconDown);
                downButton.setPreferredSize(new Dimension(32,38));
                downButton.addActionListener(a -> {
                    simManager.spawnRandomPerson(floor, ElevatorDirection.DOWN);
                });
                this.add(downButton);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
