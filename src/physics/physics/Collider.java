package physics.physics;

import physics.geometry.IntersectionalPair;
import physics.geometry.SphereIntersection;
import physics.sphere.ASS;
import physics.triangle.AST;

import java.util.ArrayList;

public class Collider {
    private final ArrayList<ASS> spheres;
    private final ArrayList<Wall> walls;
    private final ArrayList<AST> triangles;

    Collider(Space space) {
        spheres = space.spheres;
        walls = space.walls;
        triangles = space.triangles;
    }

    Collider(ArrayList<ASS> spheres, ArrayList<Wall> walls, ArrayList<AST> triangles) {
        this.spheres = spheres;
        this.walls = walls;
        this.triangles = triangles;
    }

    public void collide() throws InterruptedException {
        Thread sphereToSphereThread = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        Thread sphereToWallThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (ASS sphere : spheres){
                    for (Wall wall : walls){
                        synchronized (sphere) {
                            synchronized (wall) {
                                if (new IntersectionalPair<>(sphere, wall, true).isIntersected()) {
                                    new CollisionalPair<>(sphere, wall).collide();
                                }
                            }
                        }
                    }
                }
            }
        });

        Thread sphereToPolygonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (ASS sphere : spheres) {
                    for (AST triangle : triangles) {
                        synchronized (sphere) {
                            synchronized (triangle) {
                                if (new IntersectionalPair<>(sphere, triangle, true).isIntersected())
                                    sphere.v.setX(-100f);
                            }
                        }
                    }
                }
            }
        });

        sphereToSphereThread.start();
        sphereToPolygonThread.start();
        sphereToWallThread.start();
        sphereToSphereThread.join();
        sphereToPolygonThread.join();
        sphereToWallThread.join();

    }
}
