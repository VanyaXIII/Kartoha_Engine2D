package Kartoha_Engine2D.ui;

import Kartoha_Engine2D.drawing.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
