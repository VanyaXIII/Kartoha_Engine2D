package physics.ui;

import physics.sphere.ASS;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

public class Controller {
    private final MouseListener mouseListener;

    public Controller(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public MouseListener getMouseListener() {
        return mouseListener;
    }
}
