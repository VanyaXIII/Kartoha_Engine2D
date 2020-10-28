package physics.sphere;

import physics.drawing.DrawingPanel;
import physics.geometry.LineEq;
import physics.geometry.Vector2D;
import physics.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Main {

    public static void main(String[] args) {
        DrawingPanel panel = new DrawingPanel();
        panel.addSpace(0.05, 9.81,  1680, 1000);
        JFrame frame = new JFrame("Brownian motion");
        frame.setSize(Tools.transformDouble(panel.space.width), Tools.transformDouble(panel.space.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setLocation(100,0);
        frame.setResizable(false);
        frame.add(panel);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        panel.space.addThing(new Vector2D(20,0), 300, 500, 10);
//        panel.space.addThing(new Vector2D(-20,0), 600, 500, 10);
//        panel.space.addLine(500,1000,0,0);
        panel.space.addLine(0,1000,0,0);
        panel.space.addLine(1679,0,1679,1000);
        panel.space.addLine(0,990,1680,990);
        panel.space.addLine(0,0,300,1000);
        for (int i=0; i<20; i++) {
            int x = 500 + i*20;
            int y = 170;
            for(int j=0; j<10; j++)
            panel.space.addThing(new Vector2D(0, 0), x, y+25*j, 10);
        }

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.space.addThing(new Vector2D(20,0 ), e.getX(), e.getY(), 50, Material.Steel);
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
}

