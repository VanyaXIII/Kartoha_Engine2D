package engine.ui;

import engine.drawing.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public final class Frame extends JFrame {

    private final Scene scene;

    public Frame(Scene scene, Color bgColor, int width, int height) {
        this.scene = scene;
        this.setTitle("Engine");
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

        Thread thread = new Thread(() -> {
            try {
                for (Drawable drawable : scene.getSpace().getDrawables()) {
                    drawable.draw(finalG);

                }
            }catch (Exception ignored){}
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
