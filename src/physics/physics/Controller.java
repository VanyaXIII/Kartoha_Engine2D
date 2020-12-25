package physics.physics;

import physics.sphere.ASS;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.PrintWriter;

public class Controller {
    private KeyboardFocusManager keyboardManager;
    private ASS sphere;

    public Controller(ASS sphere) {
        keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyboardManager.addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        sphere.w += 0.3;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        sphere.w -= 0.3;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        sphere.v.setY(-200);
                    }
                }
                return false;
            }
        });
    }
}
