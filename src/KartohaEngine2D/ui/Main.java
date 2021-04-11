package KartohaEngine2D.ui;


import KartohaEngine2D.geometry.Point2;
import KartohaEngine2D.geometry.Vector2;
import KartohaEngine2D.physics.Material;
import KartohaEngine2D.physics.Space;
import KartohaEngine2D.polygons.PhysicalPolygon;
import KartohaEngine2D.utils.ImageReader;
import billiard_example.Cue;
import billiard_example.Pocket;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

final class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scene scene = new Scene(new Space(0.0025f, 000f), new Color(9, 73, 26, 255), 1600, 1000);
        scene.getSpace().addSphere(new Vector2(0,0), (float) Math.PI,1100, 500, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1130, 520, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1130, 480, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 500, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 460, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 540, 20, Material.Constantin);
        for (var a : scene.getSpace().getSpheres()){
            a.setSprite("ball.png");
        }
        scene.getSpace().addBlock(-100, -100, 2000, 200, Material.Wood);
        scene.getSpace().addBlock(1500, -100, 200, 2000, Material.Wood);
        scene.getSpace().addBlock(-100, -100, 200, 2000, Material.Wood);
        Cue cue = new Cue(ImageReader.read("kii.png"), scene, new Point2(500, 500), 400, 20);
        Pocket pocket1 = new Pocket(scene, 30, 130,130),
                pocket2 = new Pocket(scene, 30, 1470,130),
                pocket3 = new Pocket(scene, 30, 130,890),
                pocket4 = new Pocket(scene, 30, 1470,890);
        scene.getSpace().addBlock(-1000, 920, 4000, 200, Material.Wood);
        while (2 + 2 == 4) scene.update();
    }
}

