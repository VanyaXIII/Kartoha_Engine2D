package physics.physics;

import physics.geometry.IntersectionalPair;
import physics.geometry.SphereIntersection;
import physics.geometry.SphereToLineIntersection;
import physics.geometry.Vector2;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class Collider {
    private final int depth;
    private final ArrayList<ASS> spheres;
    private final ArrayList<Wall> walls;
    private final ArrayList<PhysicalPolygon> polygons;

    Collider(Space space, int depth) {
        this.depth = depth;
        spheres = space.getSpheres();
        walls = space.getWalls();
        polygons = space.getPolygons();
    }

    Collider(ArrayList<ASS> spheres, ArrayList<Wall> walls, ArrayList<PhysicalPolygon> polygons, int depth) {
        this.depth = depth;
        this.spheres = spheres;
        this.walls = walls;
        this.polygons = polygons;
    }

    public void collide() throws InterruptedException {
        for (int t = 0; t < depth; t++) {
            Thread sphereThread = new Thread(() -> {
                for (int i = 0; i < spheres.size() - 1; i++) {
                    for (int j = i + 1; j < spheres.size(); j++) {
                        synchronized (spheres.get(i)) {
                            synchronized (spheres.get(j)) {
                                if (new IntersectionalPair<>(spheres.get(i), spheres.get(j), true).isIntersected()) {
                                    new CollisionalPair<>(spheres.get(i), spheres.get(j)).collide();
                                }
                                SphereIntersection spherePair = new IntersectionalPair<>(spheres.get(i), spheres.get(j), false).getSphereIntersection();
                                if (spherePair.isIntersected) {
                                    spheres.get(i).pullSpheres(spherePair);
                                }
                            }
                        }
                    }
                }
                for (ASS sphere : spheres) {
                    for (Wall wall : walls) {
                        synchronized (sphere) {
                            synchronized (wall) {
                                if (new IntersectionalPair<>(sphere, wall, true).isIntersected()) {
                                    new CollisionalPair<>(sphere, wall).collide();
                                }
                                SphereToLineIntersection sphereAndLinePair  = new IntersectionalPair<>(sphere, wall, false).getSphereToLineIntersection();
                                if (sphereAndLinePair.isIntersected){
                                    sphere.pullSphereFromLine(sphereAndLinePair);
                                }
                            }
                        }
                    }
                }
            });
            Thread sphereToWallThread = new Thread(() -> {
                for (ASS sphere : spheres) {
                    for (Wall wall : walls) {
                        synchronized (sphere) {
                            synchronized (wall) {
                                if (new IntersectionalPair<>(sphere, wall, true).isIntersected()) {
                                    new CollisionalPair<>(sphere, wall).collide();
                                }
                            }
                        }
                    }
                }
            });

            Thread sphereToPolygonThread = new Thread(() -> {
                for (ASS sphere : spheres) {
                    for (PhysicalPolygon triangle : polygons) {
                        synchronized (sphere) {
                            synchronized (triangle) {
                                if (new IntersectionalPair<>(sphere, triangle, true).isIntersected())
                                    sphere.v.setX(-100f);
                            }
                        }
                    }
                }
            });
            Thread polygonThread = new Thread(() -> {
                for (PhysicalPolygon polygon : polygons){
                    for (Wall wall : walls){
                        synchronized (polygon){
                            synchronized (wall){
                                if (new IntersectionalPair<>(polygon, wall, true).isIntersected()){
                                    new CollisionalPair<>(polygon, wall).collide();
                                }
                            }
                        }
                    }
                }
            });
            sphereThread.start();
            polygonThread.start();
//            sphereToPolygonThread.start();
//            sphereToWallThread.start();
            sphereThread.join();
            polygonThread.join();
//            sphereToPolygonThread.join();
//            sphereToWallThread.join();
        }
    }
}
