package physics.physics;

import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class Updater {
    private final ArrayList<ASS> spheres;
    private final ArrayList<PhysicalPolygon> polygons;

    Updater(Space space){
        spheres = space.getSpheres();
        polygons = space.getPolygons();
    }

    Updater(ArrayList<ASS> spheres, ArrayList<PhysicalPolygon> polygons){
        this.spheres = spheres;
        this.polygons = polygons;
    }

    public void update() throws InterruptedException {
        Thread sphereThread = new Thread(() -> spheres.forEach(ASS::update));

        Thread polygonThread = new Thread(() -> polygons.forEach(PhysicalPolygon::update));
        sphereThread.start();
        polygonThread.start();
        sphereThread.join();
        polygonThread.join();
    }


}
