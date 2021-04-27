package com.github.gurianov.engine.ui;


import com.github.gurianov.engine.geometry.Vector2;
import com.github.gurianov.engine.physics.Material;
import com.github.gurianov.engine.physics.Space;


import java.awt.*;
import java.io.IOException;

final class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scene scene = new Scene(new Space(0.0025f, 000f), new Color(9, 73, 26, 255), 1600, 1000);
        scene.getSpace().addSphere(new Vector2(0,0), (float) Math.PI,1100, 500, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1130, 520, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1130, 480, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 500, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 460, 20, Material.Constantin);
        scene.getSpace().addSphere(new Vector2(0,0), 0,1160, 540, 20, Material.Constantin);
        scene.getSpace().addBlock(-100, -100, 2000, 200, Material.Wood);
        scene.getSpace().addBlock(1500, -100, 200, 2000, Material.Wood);
        scene.getSpace().addBlock(-100, -100, 200, 2000, Material.Wood);
        scene.getSpace().addBlock(-1000, 920, 4000, 200, Material.Wood);
        while (2 + 2 == 4) scene.update();
    }
}

