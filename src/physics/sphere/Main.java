package physics.sphere;

import physics.drawing.DrawingPanel;
import physics.geometry.Vector2;
import physics.physics.Controller;
import physics.physics.Material;
import physics.utils.Tools;



import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Main {

    public static void main(String[] args) {
        DrawingPanel panel = new DrawingPanel();
        panel.addSpace(0.01f, 300f, 1600, 1000);
        JFrame frame = new JFrame("Brownian motion");
        frame.setSize(Tools.transformFloat((float) panel.space.width), Tools.transformFloat((float) panel.space.height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK) ;
        frame.setLocation(200, 0);
        frame.setResizable(false);
        frame.add(panel);
        frame.setUndecorated(true);
        frame.setVisible(true);
//        panel.space.addWall(0,400,1000,1000);
//        panel.space.addWall(1000,400,0,1000);
//        panel.space.addLine(0, 1000, 0, 0);
//        panel.space.addTriangle(new Vector2(0,0), 0.05f, 500,600, 100, Material.LapisLazuli);
//        panel.space.addLine(900, 0, 700, 1000);
       panel.space.addWall(600,900,1400,400);
//        panel.space.addWall(1200,550,200,200);
        panel.space.addBlock(-1,900,2000,200);
//        panel.space.addBlock(1400,600-1,500,300);
//        panel.space.addBlock(100,200,100,20);
        panel.space.addThing(new Vector2(0,0), -0.4f, 600, 850, 30, Material.Steel);
//        panel.space.addThing(new Vector2(0,0), 0, 150, 160, 20, Material.Steel);
//        panel.space.addWall(0, 901, 1800, 901);
//        panel.space.addLine(0, 902, 1800, 902);
//        panel.space.addLine(0, 903, 1800, 903);
//        panel.space.addLine(0, 904, 1800, 904);
//        panel.space.addLine(0, 905, 1800, 905);
//        panel.space.addLine(0, 906, 1800, 906);
//        panel.space.addLine(050,249,700,599);
        for (int i = 0; i <100 ; i++) {
            int x = 040 + i * 30;
            int y = 000;
            for (int j = 0; j < 10; j++) {
//                panel.space.addThing(new Vector2(0f, 0f), 0.05f, x, y+50*j, 10f , Material.LapisLazuli);
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
                new Controller(panel.space.spheres.get(0));
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

