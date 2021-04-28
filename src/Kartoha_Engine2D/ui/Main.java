package Kartoha_Engine2D.ui;


import Kartoha_Engine2D.geometry.Vector2;
import Kartoha_Engine2D.physics.Material;
import Kartoha_Engine2D.physics.Space;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

final class Main {
    public static void main(String[] args) {
        Scene scene = new Scene(new Space(0.0025f, 300f), new Color(0, 0, 0, 255), 1600, 1000);
        scene.getSpace().addSphere(new Vector2(0, 0), 0f, 100, 790, 50, Material.Steel);
        scene.getSpace().addBlock(0, 800, 2000, 1000, Material.Constantin);
        scene.getSpace().addWall(500, 800, 550, 770, Material.Constantin);
        scene.getSpace().addWall(550, 770, 600, 750, Material.Constantin);
        scene.addSceneController(new SceneController(null, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                new Thread(() -> {
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                        scene.getSpace().getCamera().moveX(3);
                    if (e.getKeyCode() == KeyEvent.VK_LEFT)
                        scene.getSpace().getCamera().moveX(-3);
                    if (e.getKeyCode() == KeyEvent.VK_UP)
                        scene.getSpace().getCamera().moveY(-3);
                    if (e.getKeyCode() == KeyEvent.VK_DOWN)
                        scene.getSpace().getCamera().moveY(3);
                }).start();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        }, null));
        scene.addObjectController(new ObjectController(scene.getSpace().getSpheres().get(0), new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                new Thread(() -> {
                    if (e.getKeyCode() == KeyEvent.VK_W)
                        scene.getSpace().getSpheres().get(0).setW(scene.getSpace().getSpheres().get(0).getW() + 0.15f);
                    if (e.getKeyCode() == KeyEvent.VK_S)
                        scene.getSpace().getSpheres().get(0).setW(scene.getSpace().getSpheres().get(0).getW() - 0.15f);
                    if (e.getKeyCode() == KeyEvent.VK_SPACE)
                        scene.getSpace().getSpheres().get(0).getV().add(new Vector2(0,-300));
                }).start();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        }, null, null, null));
        while (2 + 2 == 4) scene.update();
    }
}

