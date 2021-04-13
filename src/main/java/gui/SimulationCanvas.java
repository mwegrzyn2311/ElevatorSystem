package gui;

import elevator_system.ElevatorSystem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;

public class SimulationCanvas extends Canvas {
    private final int elCount;
    private static final int vGap = 6;
    private static final int hGap = 4;
    private static final int lobbyWidth = 128;
    private static final int elWidth = 64;
    private static final int elHeight = 64;
    private static final int padding = 3;
    private static final int peopleDist = 8;
    private static final int elevatorWallWidth = 2;

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
        g.setFont(g.getFont().deriveFont(16.0f));
        for(int i = 0; i <= (maxFloor - minFloor + 1); ++i) {
            g.fillRect(0, i * (elHeight + vGap + 2 * padding), width, vGap);
            // Floor number label
            g.drawRect(vGap + lobbyWidth + padding * 2 - 16, i * (elHeight + vGap + 2 * padding) + vGap, 18, 18);
            g.drawString(String.valueOf(maxFloor - i), vGap + lobbyWidth + padding * 2 - 16 + 4, i * (elHeight + vGap + 2 * padding) + 16 + vGap);
        }
        g.fillRect(0, 0, hGap, height);
        for(int i = 0; i < elCount; ++i) {
            g.fillRect((lobbyWidth + padding * 2) + hGap * 2 + i * (elWidth + 2 * padding + hGap), 0, hGap, height);
        }

        // Elevators with people inside
        elSystem.elevators.forEach(el -> {
            g.drawImage(
                    elevatorImage,
                    (lobbyWidth + padding * 2) + hGap * 2 + el.id * (elWidth + 2 * padding + hGap) + hGap + padding,
                    (elHeight + 2 * padding + vGap) * (-el.currentFloor + maxFloor) + vGap + padding,
                    null);
            // TODO: Handle there being more people than can fit in the elevator
            for (int i = 0; i < el.peopleInside.size(); ++i) {
                g.drawImage(
                        manImage,
                        (lobbyWidth + padding * 2) + hGap * 2 + el.id * (elWidth + 2 * padding + hGap) + hGap + padding + elevatorWallWidth + peopleDist * i,
                        (elHeight + 2 * padding + vGap) * (-el.currentFloor + maxFloor) + vGap + padding,
                        null
                );
            }

        });
        // People in the lobby
        for(int i = 0; i < elSystem.waitingPeople.size(); ++i) {
            for(int j = 0; j < elSystem.waitingPeople.get(i).size(); j++) {
                g.drawImage(
                        manImage,
                        hGap + padding + j * peopleDist,
                        (elHeight + 2 * padding + vGap) * (maxFloor - minFloor - i) + vGap + padding,
                        null
                );
            }
        }
        elSystem.waitingPeople.forEach(peopleOnFloor -> {

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
