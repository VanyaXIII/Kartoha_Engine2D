package Kartoha_Engine2D.ui;

import java.awt.event.*;

public class ObjectController {

    private final Controllable object;
    private final KeyListener keyListener;
    private final MouseListener mouseListener;
    private final MouseMotionListener mouseMotionListener;
    private final MouseWheelListener mouseWheelListener;

    public ObjectController(Controllable object, KeyListener keyListener, MouseListener mouseListener, MouseMotionListener mouseMotionListener, MouseWheelListener mouseWheelListener){
        this.object = object;
        this.mouseListener = mouseListener;
        this.keyListener = keyListener;
        this.mouseMotionListener = mouseMotionListener;
        this.mouseWheelListener = mouseWheelListener;
    }

    public Controllable getObject() {
        return object;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }

    public MouseWheelListener getMouseWheelListener() {
        return mouseWheelListener;
    }
}
