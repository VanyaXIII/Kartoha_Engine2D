package physics.drawing;

import physics.physics.Space;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {
    public Space space;
    private boolean fpsFlag = true;

    public void addSpace(float dt, float g, int width, int height) {
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
                g.setColor(Color.WHITE);
                fpsFlag = !fpsFlag;
                g.drawString(String.valueOf(space.getFps()), 20, 20);
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        space.printEnergy();
    }
}
