package physics.drawing;

import physics.geometry.LineEq;
import physics.sphere.ASS;
import physics.sphere.Space;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    public Space space;

    public void addSpace(double dt, double g, int width, int height) {
        space = new Space(dt, g, width, height);
    }

    @Override
    public void paint(Graphics g) {
        space.changeTime();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Drawable drawable : space.drawables) {
                    drawable.draw(g);

                }
            }
        });
        thread.start();
//        for (Drawable drawable : space.drawables){
//            drawable.draw(g);
//        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        space.printEnergy();
    }
}
