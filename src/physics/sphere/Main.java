package physics.sphere;

import physics.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Main extends JPanel {
    private final static Space space = new Space(0.05, 10,  1680, 1000);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Brownian motion");
        frame.setSize(Tools.transformDouble(space.width), Tools.transformDouble(space.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setLocation(100,0);
        frame.setResizable(false);
        Main panel = new Main();
        frame.add(panel);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        panel.space.addThing(new Vector2D(20,0), 300, 500, 10);
//        panel.space.addThing(new Vector2D(-20,0), 600, 500, 10);
//        panel.space.addLine(500,1000,0,0);
        panel.space.addLine(0,1000,0,0);
        panel.space.addLine(1679,0,1679,1000);
        panel.space.addLine(0,999,1680,999);
        panel.space.addLine(0,0,300,1000);
        for (int i=0; i<40; i++) {
            int x = 200 + i*20;
            int y = 100;
            for(int j=0; j<10; j++)
            panel.space.addThing(new Vector2D(0, 0), x, y+25*j, 10);
        }

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.space.addThing(new Vector2D(0, 100), e.getX(), e.getY(), 50, Material.Steel);
                panel.space.printThings();
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        while (true) {
            frame.repaint();
        }
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
//                for (AST thing : space.things) {
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
        for (AST thing : space.things) {
            //System.out.println(thing.v.length());
            thing.draw(g);
            //g.drawLine(thing.getVectorCoords()[0],thing.getVectorCoords()[1],thing.getVectorCoords()[2],thing.getVectorCoords()[3]);
        }
        //g.drawOval(gg.drawcoords()[0],gg.drawcoords()[1],gg.drawcoords()[2],gg.drawcoords()[2]);
        space.changeTime();
        space.printEnergy();


    }
}
