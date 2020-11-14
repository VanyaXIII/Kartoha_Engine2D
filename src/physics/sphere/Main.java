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
        panel.addSpace(0.03f, 0.1f, 1600, 1000);
        JFrame frame = new JFrame("Brownian motion");
        frame.setSize(Tools.transformFloat((float) panel.space.width), Tools.transformFloat((float) panel.space.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setLocation(100, 0);
        frame.setResizable(false);
        frame.add(panel);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        panel.space.addLine(0,1000,1000,400);
//        panel.space.addLine(0, 1000, 0, 0);
//        panel.space.addTriangle(new Vector2(0,0), 0.05, 500,600, 100, Material.LapisLazuli);
//        panel.space.addLine(900, 0, 700, 1000);
        panel.space.addWall(500,500,1000,1000);
//        panel.space.addLine(0, 901, 1800, 901);
//        panel.space.addLine(0, 902, 1800, 902);
//        panel.space.addLine(0, 903, 1800, 903);
//        panel.space.addLine(0, 904, 1800, 904);
//        panel.space.addLine(0, 905, 1800, 905);
//        panel.space.addLine(0, 906, 1800, 906);
//        panel.space.addLine(050,249,700,599);
        for (int i = 0; i <10 ; i++) {
            int x = 740 + i * 30;
            int y = 200;
            for (int j = 0; j < 10; j++) {
//                panel.space.addThing(new Vector2(0f, 1f), 0.0f, x, y+20*j, 10f , Material.LapisLazuli);
            }
        }

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                panel.space.addThing(new Vector2(0,0), 0.07f, e.getX(), e.getY(), 20, Material.Steel);
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

