package Kartoha_Engine2D.ui;

import Kartoha_Engine2D.physics.Space;
import lombok.Getter;

import java.awt.*;

public class Scene {

    @Getter
    private final Space space;
    @Getter
    private final Frame frame;


    public Scene(Space space, Color bgColor, int width, int height) {
        this.space = space;
        frame = new Frame(this, bgColor, width, height);
    }



    public void addObjectController(ObjectController objectController) {
        frame.addKeyListener(objectController.getKeyListener());
        frame.addMouseMotionListener(objectController.getMouseMotionListener());
        frame.addMouseListener(objectController.getMouseListener());
        frame.addMouseWheelListener(objectController.getMouseWheelListener());
    }

    public void addSceneController(SceneController sceneController) {
        frame.addKeyListener(sceneController.getKeyListener());
        frame.addMouseMotionListener(sceneController.getMouseMotionListener());
        frame.addMouseListener(sceneController.getMouseListener());
    }

    public void update() {
        space.changeTime();
        frame.repaint();
    }

}
