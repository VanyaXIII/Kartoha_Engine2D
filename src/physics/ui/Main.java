package physics.ui;


import physics.geometry.Vector2;
import physics.physics.Material;
import physics.physics.Space;
import physics.utils.Tools;



import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Main {

    public static void main(String[] args) {
        Scene scene = new Scene(new Space(0.01f, 300f), Color.BLACK, 1600, 1000);
      scene.getSpace().addWall(1400,600,600,900);
        scene.getSpace().addWall(1200,550,200,200);
        scene.getSpace().addBlock(-1,900,2000,200);
        while (true) scene.update();
    }
}

