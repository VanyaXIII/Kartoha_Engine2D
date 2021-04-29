package climber_example.level_creator;

import Kartoha_Engine2D.physics.Space;
import Kartoha_Engine2D.ui.Scene;
import Kartoha_Engine2D.ui.SceneController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LevelCreator {

    public static void main(String[] args) {
        Scene scene = new Scene(new Space(0.0025f, 0f), Color.BLACK, 1200, 800);
        Controller controller = new Controller(scene);
        scene.addSceneController(controller.getSceneController());
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
        while (true) scene.update();
    }


}
