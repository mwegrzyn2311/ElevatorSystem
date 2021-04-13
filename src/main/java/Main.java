import gui.ElevatorsFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Missing some arguments. Please provide: [elCount, minFloor, maxFloor]");
        }
        final int elCount = Integer.parseInt(args[0]);
        final int minFloor = Integer.parseInt(args[1]);
        final int maxFloor = Integer.parseInt(args[2]);

        SwingUtilities.invokeLater(() -> new ElevatorsFrame(elCount, minFloor, maxFloor));
    }
}
