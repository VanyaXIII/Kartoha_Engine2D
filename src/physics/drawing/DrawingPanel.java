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
//        Thread thread1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (LineEq line : space.lines) {
//                    g.setColor(Color.ORANGE);
//                    g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
//                }
//                for (ASS thing : space.things) {
//                    //System.out.println(thing.v.length());
//                    thing.draw(g);
//                    //g.drawLine(thing.getVectorCoords()[0],thing.getVectorCoords()[1],thing.getVectorCoords()[2],thing.getVectorCoords()[3]);
//                }
//            }
//        });
//        thread1.start();
        for (LineEq line : space.lines) {
            g.setColor(Color.ORANGE);
            g.drawLine((int) line.x1, (int) line.y1, (int) line.x2, (int) line.y2);
        }
        for (ASS thing : space.things) {
            //System.out.println(thing.v.length());
            thing.draw(g);
            //g.drawLine(thing.getVectorCoords()[0],thing.getVectorCoords()[1],thing.getVectorCoords()[2],thing.getVectorCoords()[3]);
        }
        //g.drawOval(gg.drawcoords()[0],gg.drawcoords()[1],gg.drawcoords()[2],gg.drawcoords()[2]);
        space.changeTime();
//        space.printEnergy();
    }
}
