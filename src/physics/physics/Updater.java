package physics.physics;

import physics.sphere.ASS;
import physics.triangle.AST;

import java.util.ArrayList;

public class Updater {
    private final ArrayList<ASS> spheres;
    private final ArrayList<AST> triangles;

    Updater(Space space){
        spheres = space.getSpheres();
        triangles = space.getTriangles();
    }

    Updater(ArrayList<ASS> spheres, ArrayList<AST> triangles){
        this.spheres = spheres;
        this.triangles = triangles;
    }

    public void update() throws InterruptedException {
        Thread sphereThread = new Thread(() -> {
            for (ASS sphere : spheres) sphere.update();
        });

        Thread polygonThread = new Thread(() -> {
            for (AST triangle : triangles) triangle.update();
        });
        sphereThread.start();
        polygonThread.start();
        sphereThread.join();
        polygonThread.join();
    }


}
