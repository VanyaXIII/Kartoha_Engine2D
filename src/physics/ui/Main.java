package physics.ui;


import physics.physics.Space;

import java.awt.*;

class Main {

    public static void main(String[] args) {
        Scene scene = new Scene(new Space(0.0025f, 300f), Color.BLACK, 1600, 1000);
        scene.getSpace().addWall(1400, 600, 600, 900);
        scene.getSpace().addWall(1400, 600, 100, 200);
        scene.getSpace().addWall(600, 900, 100, 200);
        scene.getSpace().addBlock(-1000, 900, 4000, 200);
        while (2 + 2 == 4) scene.update();
    }
}

