package Kartoha_Engine2D.ui;

import Kartoha_Engine2D.drawing.ArbitraryFigure;
import Kartoha_Engine2D.drawing.Drawable;
import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.physics.Block;
import Kartoha_Engine2D.physics.Space;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.*;

public final class Frame extends JFrame {

    private final Scene scene;

    public Frame(Scene scene, Color bgColor, int width, int height) {
        this.scene = scene;
        this.setTitle("Kartoha Engine 2D");
        this.setLocation(200, 0);
        this.setSize(width, height);
        this.setBackground(bgColor);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);

    }

    @Override
    public void paint(Graphics g) {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }

        g = bufferStrategy.getDrawGraphics();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics finalG = g;

        Graphics finalG1 = g;
        Thread thread = new Thread(() -> {
            try {
                LinkedList<Set<Drawable>> drawablesSet = new LinkedList<>();
                int z = 0;
                int counter;
                do {
                    counter = 0;
                    drawablesSet.add(new HashSet<>());
                    for (Drawable drawable : scene.getSpace().getDrawables()) {
                        if (drawable.getZ() == z) {
                            drawablesSet.getLast().add(drawable);
                            counter++;
                        }
                    }
                    z++;
                } while (counter != 0);
                for (Set<Drawable> set : drawablesSet) {
                    for (Drawable drawable : set) {
                        drawable.draw(finalG1);
                    }
                }
                Space space = scene.getSpace();
                int n = space.getWalls().size();
                ArrayList<Point2> points = new ArrayList<>();
                for(int i = 0; i < n; i++){
                    if (i == 0){
                        points.add(new Point2(space.getWalls().get(0).getPoint1().x, 3000));
                    }
                    points.add(space.getWalls().get(i).getPoint1());
                    points.add(space.getWalls().get(i).getPoint2());
                    if (i == n -1){
                        points.add(new Point2(space.getWalls().get(n-1).getPoint2().x, 3000));
                    }
                }
                ArbitraryFigure arbitraryFigure = new ArbitraryFigure(points);
                finalG1.setColor(space.getWalls().get(0).getMaterial().fillColor);
                finalG1.fillPolygon(arbitraryFigure.getPolygon(space.getCamera()));
            } catch (Exception ignored) {
            }
            finalG.setColor(Color.WHITE);
            finalG.drawString(String.valueOf(scene.getSpace().getFps()), 20, 44);
        }
        );
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        g.dispose();
        bufferStrategy.show();
    }
}
