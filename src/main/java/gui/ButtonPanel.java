package gui;

import manager.SimulationManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ButtonPanel extends JPanel {
    private final SimulationManager simManager;

    private final JButton pauseButton = new JButton();
    private final JButton nextStepButton = new JButton();
    private final JButton prevStepButton = new JButton();
    private final JButton restartSimButton = new JButton();

    public ButtonPanel(SimulationManager simManager) {
        this.simManager = simManager;

        initButtonProperties();
        initButtonHandlers();
        this.add(pauseButton);
        this.add(nextStepButton);
        this.add(prevStepButton);
        this.add(restartSimButton);
    }

    private void initButtonProperties() {
        try {
            pauseButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/pause.png"))));
            nextStepButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/speedup.png"))));
            prevStepButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/slowdown.png"))));
            restartSimButton.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/images/refresh.png")).getScaledInstance(32, 32, Image.SCALE_DEFAULT)));
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        pauseButton.setPreferredSize(new Dimension(64,42));
        nextStepButton.setPreferredSize(new Dimension(64,42));
        prevStepButton.setPreferredSize(new Dimension(64,42));
        restartSimButton.setPreferredSize(new Dimension(64, 42));
    }
    private void initButtonHandlers() {
        pauseButton.addActionListener(a -> {

        });
        nextStepButton.addActionListener(a -> {
            simManager.nextStep();
        });
        prevStepButton.addActionListener(a -> {

        });
        restartSimButton.addActionListener(a -> {
            simManager.quickRestart();
        });
    }
}
