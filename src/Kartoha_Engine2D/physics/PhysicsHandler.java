package Kartoha_Engine2D.physics;

import Kartoha_Engine2D.geometry.*;
import Kartoha_Engine2D.sphere.PhysicalSphere;
import Kartoha_Engine2D.polygons.PhysicalPolygon;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class PhysicsHandler {

    private final ArrayList<PhysicalSphere> spheres;
    private final ArrayList<PhysicalPolygon> polygons;
    private final ArrayList<Wall> walls;
    private final int depth;

    PhysicsHandler(Space space, int depth) {
        spheres = space.getSpheres();
        polygons = space.getPolygons();
        walls = space.getWalls();
        this.depth = depth;
    }

    public void update() throws InterruptedException, ConcurrentModificationException {
        for (int t = 0; t < depth; t++) {

            Thread sphereThread = new Thread(() -> {

                for (int i = 0; i < spheres.size() - 1; i++) {
                    for (int j = i + 1; j < spheres.size(); j++) {
                        synchronized (spheres.get(i)) {
                            synchronized (spheres.get(j)) {
                                if (new IntersectionalPair<>(spheres.get(i), spheres.get(j)).areIntersected()) {
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
                synchronized (spheres) {
                    spheres.forEach(sphere -> {
                        walls.forEach(wall -> {
                            synchronized (sphere) {
                                synchronized (wall) {
                                    if (new IntersectionalPair<>(sphere, wall.toLine()).areIntersected()) {
                                        new CollisionalPair<>(sphere, wall).collide();
                                    }
                                    SphereToLineIntersection sphereAndLinePair = new IntersectionalPair<>(sphere, wall.toLine()).getSphereToLineIntersection();
                                    if (sphereAndLinePair.isIntersected) sphere.pullFromLine(sphereAndLinePair);
                                }
                            }
                        });
                    });
                }

            });

            Thread polygonThread = new Thread(() -> {

                for (int i = 0; i < polygons.size(); i++) {
                    for (int j = 0; j < polygons.size(); j++) {
                        if (i != j && new IntersectionalPair<>(polygons.get(i), polygons.get(j)).areIntersected()) {
                            new CollisionalPair<>(polygons.get(i), polygons.get(j)).collide();
                        }

                        for (Line line : polygons.get(j).getLines(false)) {
                            PolygonToLineIntersection polygonAndWallPair =
                                    new IntersectionalPair<>(polygons.get(i), line).getPolygonToLineIntersection();
                            if (polygonAndWallPair.isIntersected) {
                                polygons.get(i).pullFromLine(polygonAndWallPair);
                            }
                        }
                    }
                }

                polygons.forEach(polygon -> {

                    walls.forEach(wall -> {
                        synchronized (polygon) {
                            synchronized (wall) {
                                if (new IntersectionalPair<>(polygon, wall.toLine()).areIntersected()) {
                                    new CollisionalPair<>(polygon, wall).collide();
                                }
                                PolygonToLineIntersection polygonAndWallPair =
                                        new IntersectionalPair<>(polygon, wall.toLine()).getPolygonToLineIntersection();
                                if (polygonAndWallPair.isIntersected) {
                                    polygon.pullFromLine(polygonAndWallPair);
                                }
                            }
                        }
                    });

                    spheres.forEach(sphere -> {
                        synchronized (polygon) {
                            synchronized (sphere) {
                                for (Line line : polygon.getLines(true)) {
                                    if (new IntersectionalPair<>(line, sphere).areIntersected()) {
                                        new CollisionalPair<>(polygon, sphere).collide();
                                    }
                                    SphereToLineIntersection sphereAndLinePair = new IntersectionalPair<>(sphere, line).getSphereToLineIntersection();
                                    if (sphereAndLinePair.isIntersected) sphere.pullFromLine(sphereAndLinePair);
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

            sphereThread = new Thread(() -> {
                synchronized (spheres) {
                    spheres.forEach(PhysicalSphere::update);
                }
            });

            polygonThread = new Thread(() -> {
                synchronized (polygons) {
                    polygons.forEach(PhysicalPolygon::update);
                }
            }
            );

            sphereThread.start();
            polygonThread.start();
            sphereThread.join();
            polygonThread.join();
        }


    }
}
