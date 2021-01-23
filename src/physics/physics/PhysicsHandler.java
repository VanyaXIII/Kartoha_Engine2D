package physics.physics;

import physics.geometry.*;
import physics.sphere.ASS;
import physics.polygons.PhysicalPolygon;

import java.util.ArrayList;

public class PhysicsHandler {
    private final ArrayList<ASS> spheres;
    private final ArrayList<PhysicalPolygon> polygons;
    private final ArrayList<Wall> walls;
    private final int depth;

    PhysicsHandler(Space space, int depth) {
        spheres = space.getSpheres();
        polygons = space.getPolygons();
        walls = space.getWalls();
        this.depth = depth;
    }

    public void update() throws InterruptedException {
        for (int t = 0; t < depth; t++) {
            Thread sphereThread = new Thread(() -> {
                for (int i = 0; i < spheres.size() - 1; i++) {
                    for (int j = i + 1; j < spheres.size(); j++) {
                        synchronized (spheres.get(i)) {
                            synchronized (spheres.get(j)) {
                                if (new IntersectionalPair<>(spheres.get(i), spheres.get(j)).isIntersected()) {
                                    new CollisionalPair<>(spheres.get(i), spheres.get(j)).collide();
                                }
                                SpheresIntersection spherePair = new IntersectionalPair<>(spheres.get(i), spheres.get(j)).getSpheresIntersection();
                                if (spherePair.isIntersected) {
                                    spheres.get(i).pullFromSphere(spherePair);
                                }
                            }
                        }
                    }
                }
                spheres.forEach(sphere -> {
                    walls.forEach(wall -> {
                        synchronized (sphere) {
                            synchronized (wall) {
                                if (new IntersectionalPair<>(sphere, wall).isIntersected()) {
                                    new CollisionalPair<>(sphere, wall).collide();
                                }
                                SphereToLineIntersection sphereAndLinePair = new IntersectionalPair<>(sphere, wall).getSphereToLineIntersection();
                                if (sphereAndLinePair.isIntersected) sphere.pullFromLine(sphereAndLinePair);
                            }
                        }
                    });
                });
            });
            Thread polygonThread = new Thread(() -> {
                polygons.forEach(polygon -> {
                    walls.forEach(wall -> {
                        synchronized (polygon) {
                            synchronized (wall) {
                                if (new IntersectionalPair<>(polygon, wall).isIntersected()) {
                                    new CollisionalPair<>(polygon, wall).collide();
                                }
                                PolygonToLineIntersection polygonAndWallPair =
                                        new IntersectionalPair<>(polygon, wall).getPolygonToLineIntersection();
                                if (polygonAndWallPair.isIntersected) {
                                    polygon.pullFromLine(polygonAndWallPair);
                                }
                            }
                        }
                    });

                    spheres.forEach(sphere ->  {
                        synchronized (polygon){
                            synchronized (sphere){
                                if (new IntersectionalPair<>(polygon, sphere).isIntersected()){
                                    sphere.setV(new Vector2(0, 0));
                                    polygon.setW(0);
                                }
                            }
                        }
                    });
                });
            });
            sphereThread.start();
            polygonThread.start();
            sphereThread.join();
            polygonThread.join();
            sphereThread = new Thread(() -> spheres.forEach(ASS::update));

            polygonThread = new Thread(() -> polygons.forEach(PhysicalPolygon::update));
            sphereThread.start();
            polygonThread.start();
            sphereThread.join();
            polygonThread.join();
        }


    }
}
