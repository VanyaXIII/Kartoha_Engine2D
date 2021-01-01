package physics.physics;

import physics.sphere.ASS;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Controller {

    public Controller(ASS sphere) {
        KeyboardFocusManager keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyboardManager.addKeyEventDispatcher(e -> {
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
        });
    }
}
