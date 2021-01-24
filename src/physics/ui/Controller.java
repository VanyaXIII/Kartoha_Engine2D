package physics.ui;

import physics.sphere.ASS;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class Controller {

    private final MouseListener mouseListener;
    private final KeyListener keyListener;

    public Controller(MouseListener mouseListener, KeyListener keyListener) {
        this.mouseListener = mouseListener;
        this.keyListener = keyListener;
    }


    public MouseListener getMouseListener() {
        return mouseListener;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }
}
