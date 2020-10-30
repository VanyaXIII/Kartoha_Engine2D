package physics.sphere;

import physics.drawing.DrawingPanel;
import physics.geometry.Vector2;
import physics.utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Main {

    public static void main(String[] args) {
        DrawingPanel panel = new DrawingPanel();
        panel.addSpace(0.015, 0.04, 1600, 1000);
        JFrame frame = new JFrame("Brownian motion");
        frame.setSize(Tools.transformDouble(panel.space.width), Tools.transformDouble(panel.space.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setLocation(100, 0);
        frame.setResizable(false);
        frame.add(panel);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        panel.space.addLine(500,1000,0,0);
        panel.space.addLine(0, 1000, 0, 0);
//        panel.space.addTriangle(new Vector2(1,0), 0.00, 500,600, 100, Material.LapisLazuli);
//        panel.space.addLine(0, 700, 1100, 700);
//        panel.space.addLine(400, 0, 600, 400);
//        panel.space.addLine(900, 0, 700, 400);
//        panel.space.addThing(new Vector2(4, 0), 200, 200, 10, Material.Osmium);
        panel.space.addLine(0, 900, 1800, 900);
        panel.space.addLine(0,0,500,1000);
        for (int i = 0; i < 20; i++) {
            int x = 540 + i * 20;
            int y = 40;
            for (int j = 0; j < 20; j++) {
                panel.space.addThing(new Vector2(0, 0), x, y+20*j, 10 , Material.LapisLazuli);
            }
        }

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.space.addTriangle(new Vector2(0, 0), 0.5, e.getX(), e.getY(), 100, Material.LapisLazuli);
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

