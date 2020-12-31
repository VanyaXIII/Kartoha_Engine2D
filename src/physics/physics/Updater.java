package physics.physics;

import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class Updater {
    private final ArrayList<ASS> spheres;
    private final ArrayList<PhysicalPolygon> triangles;

    Updater(Space space){
        spheres = space.getSpheres();
        triangles = space.getPolygons();
    }

    Updater(ArrayList<ASS> spheres, ArrayList<PhysicalPolygon> triangles){
        this.spheres = spheres;
        this.triangles = triangles;
    }

    public void update() throws InterruptedException {
        Thread sphereThread = new Thread(() -> {
            for (ASS sphere : spheres) sphere.update();
        });

        Thread polygonThread = new Thread(() -> {
            for (PhysicalPolygon triangle : triangles) triangle.update();
        });
        sphereThread.start();
        polygonThread.start();
        sphereThread.join();
        polygonThread.join();
    }


}
