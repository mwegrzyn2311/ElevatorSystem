package gui;

import elevator_system.ElevatorSystem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class SimulationCanvas extends Canvas {
    private final int elCount;
    private static final int vGap = 5;
    private static final int hGap = 4;
    private static final int lobbyWidth = 128;
    private static final int elWidth = 64;
    private static final int elHeight = 64;
    private static final int padding = 3;

    private final int width;
    private final int height;
    private final int minFloor;
    private final int maxFloor;

    public final Image manImage = getImageFromResources("/sprites/man.png");
    public final Image elevatorImage = getImageFromResources("/sprites/elevator.png");

    private final ElevatorSystem elSystem;

    public SimulationCanvas(ElevatorSystem elSystem) {
        this.elSystem = elSystem;
        elCount = elSystem.elevatorsCount;
        minFloor = elSystem.minFloor;
        maxFloor = elSystem.maxFloor;
        width = lobbyWidth + 2 * padding + 2 * hGap + (elCount) * (elWidth + padding * 2 + hGap);
        height = (elHeight + 2 * padding) * (maxFloor - minFloor + 1) + (maxFloor - minFloor + 2) * vGap;
        Dimension size = new Dimension(width, height);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(2);
            bs = getBufferStrategy();
        }
        Graphics g = bs.getDrawGraphics();


        g.clearRect(0,0, width, height);

        // Background
        g.setColor(Color.BLACK);
        for(int i = 0; i <= (maxFloor - minFloor + 1); ++i) {
            g.fillRect(0, i * (elHeight + vGap + 2 * padding), width, vGap);
        }
        g.fillRect(0, 0, hGap, height);
        for(int i = 0; i < elCount; ++i) {
            g.fillRect((lobbyWidth + padding * 2) + hGap * 2 + i * (elWidth + 2 * padding + hGap), 0, hGap, height);
        }

        // Elevators
        elSystem.elevators.forEach(el -> {
            g.drawImage(
                    elevatorImage,
                    (lobbyWidth + padding * 2) + hGap * 2 + el.id * (elWidth + 2 * padding + hGap) + hGap + padding,
                    (elHeight + 2 * padding + vGap) * (-el.currentFloor + maxFloor) + vGap + padding,
                    null);
        });

        g.dispose();
        bs.show();
    }

    @Override
    public void paint(Graphics g) {
        render();
    }





    private Image getImageFromResources(String path) {
        Image res = null;
        try {
            res = ImageIO.read(getClass().getResource(path));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
