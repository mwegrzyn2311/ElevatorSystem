package gui;

import manager.SimulationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ButtonPanel extends JPanel {
    private final SimulationManager simManager;

    private final JButton nextStepButton = new JButton();
    private final JButton restartSimButton = new JButton();
    private final JButton infoButton = new JButton("Info");

    public ButtonPanel(SimulationManager simManager) {
        this.simManager = simManager;

        initButtonProperties();
        initButtonHandlers();
        this.add(nextStepButton);
        this.add(restartSimButton);
        this.add(infoButton);
    }

    private void initButtonProperties() {
        try {
            nextStepButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/speedup.png"))));
            restartSimButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/refresh.png")).getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        nextStepButton.setPreferredSize(new Dimension(64,42));
        restartSimButton.setPreferredSize(new Dimension(64, 42));
        infoButton.setPreferredSize(new Dimension(64, 42));
    }
    private void initButtonHandlers() {
        nextStepButton.addActionListener(a -> {
            simManager.nextStep();
        });
        restartSimButton.addActionListener(a -> {
            simManager.hardRestart();
        });
        infoButton.addActionListener(a -> {
            simManager.printInfo();
        });
    }
}
