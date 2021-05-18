package Kartoha_Engine2D.ui;


import Kartoha_Engine2D.drawing.camera.Resolution;
import Kartoha_Engine2D.geometry.Point2;
import Kartoha_Engine2D.geometry.Vector2;
import Kartoha_Engine2D.physics.Material;
import Kartoha_Engine2D.physics.Space;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.utils.BuildParams;
import Kartoha_Engine2D.utils.JsonReader;
import climber_example.Level;
import climber_example.boosters.Applicable;
import climber_example.boosters.Booster;
import climber_example.boosters.Boosters;
import climber_example.level_creator.Container;
import lombok.SneakyThrows;


import java.awt.*;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.lang.reflect.Method;

final class Main {
    @SneakyThrows
    public static void main(String[] args) {

        BuildParams params = BuildParams.BY_LEVEL;

        Scene scene = new Scene(new Space(0.0025f, 300f), new Color(0, 0, 0, 255), 1600, 1000);
        scene.getSpace().initCamera(new Resolution(1600, 1000));
        Container container = new Container(scene.getSpace());




        if (params == BuildParams.BY_LEVEL) {
            loadLevel(container);
        }
        if (params == BuildParams.RANDOM) {
            Point2[] points = new Point2[100];
            for (int i = 0; i < 100; i++) {
                points[i] = new Point2((int) (i * 50 * (1 + Math.sqrt(2))), (int) (800 + (Math.random() - 0.5) * 2 * 50 * 0.5 * (1 + Math.sqrt(2))));
            }
            for (int i = 1; i < 100; i++) {
                scene.getSpace().addWall(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y, Material.WOOD);
            }
        }

        scene.getSpace().addSphere(new Vector2(0, 0), 0f, 100, 690, 50, Material.WOOD);
        scene.getSpace().getSpheres().get(0).setZ(0);
        scene.getSpace().focusCameraOnObject(scene.getSpace().getSpheres().get(0));
        Applicable applicable = new Applicable() {
            @Override
            public void apply() {
                scene.getSpace().setG(0);
                scene.getSpace().getSpheres().get(0).setV(new Vector2(0,0));
                scene.getSpace().getSpheres().get(0).setW(0);
            }

            @Override
            public void disable() {
                scene.getSpace().getSpheres().get(0).setCords(new Point2(0,0));
            }
        };
        Booster booster = new Booster(500, applicable);
        booster.setDrawingParams(new PhysicalSphere(scene.getSpace(), 500, 500, 500));
        scene.getSpace().getDrawables().add(booster);
        scene.getSpace().getExecutables().add(() -> {
            booster.reduceDuration();
            if (booster.checkCollision(scene.getSpace().getSpheres().get(0)))
                booster.apply();
            booster.check();
        });
        scene.addSceneController(new SceneController(null, new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                new Thread(() -> {
                    if (e.getKeyCode() == KeyEvent.VK_Q)
                        saveLevel(scene);
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

    private static void loadLevel(Container container){
        FileDialog dialog = new FileDialog((Frame) null);
        dialog.setVisible(true);
        String directory = dialog.getDirectory();
        String filename = dialog.getFile();
        dialog.dispose();
        if (directory != null && filename != null) {
            String path = directory + filename;
            try {
                container.setLevel((Level) (new JsonReader(path).read(Level.class)));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private static void saveLevel(Scene scene){
        new Level(scene.getSpace()).save();
    }
}

