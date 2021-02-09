package physics.ui;

import physics.sphere.ASS;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Controller {

    private final MouseListener mouseListener;
    private final MouseMotionListener mouseMotionListener;
    private final KeyListener keyListener;

    public Controller(MouseListener mouseListener, KeyListener keyListener, MouseMotionListener mouseMotionListener) {
        this.mouseListener = mouseListener;
        this.keyListener = keyListener;
        this.mouseMotionListener = mouseMotionListener;
    }


    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public MouseMotionListener getMouseMotionListener() {
        return mouseMotionListener;
    }
}
