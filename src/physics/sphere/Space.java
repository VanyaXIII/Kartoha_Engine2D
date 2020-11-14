package physics.sphere;

import physics.drawing.Drawable;
import physics.geometry.Line;
import physics.geometry.Vector2;
import physics.triangle.AST;
import physics.utils.Tools;
import physics.utils.threads.SphereThread;
import physics.utils.threads.TriangleThread;

import java.awt.*;
import java.util.ArrayList;

//TODO бить пространство на части
//TODO слушатель столкновений и всего такого, не забыть про wait()
public class Space {
    public ArrayList<Wall> walls;
    public ArrayList<ASS> spheres;
    public ArrayList<ASS> countableSpheres;
    public ArrayList<Drawable> drawables;
    public ArrayList<AST> triangles;
    public final float dt;
    public final float g;
    public final double height, width;
    private double time;
    private double correctEn;
    private double energy;
    private int amOfTh;

    public Space(float dt, float g, int width, int height) {
        this.dt = dt;
        this.g = g;
        this.time = 0;
        this.height = height;
        this.width = width;
        walls = new ArrayList<>();
        spheres = new ArrayList<>();
        triangles = new ArrayList<>();
        drawables = new ArrayList<>();
        correctEn = 0.0;
        energy = 0.0;
        amOfTh = 0;
    }

    public void changeTime() {
        long time1 = System.nanoTime();
        countableSpheres = (ArrayList<ASS>) spheres.clone();
        SphereThread sthread = new SphereThread(this);
        TriangleThread tthread = new TriangleThread(this);
        sthread.start();
        tthread.start();
        try {
            sthread.join();
            tthread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ASS.collisionMode = !ASS.collisionMode;
        float cTime = (float) ((System.nanoTime() - time1) / 1000000.0);
//        System.out.println("Counting: " + cTime);
        countEn();
//        fixEnergy();
        float sleepTime = 0;
        if (dt * 1000.0 - cTime > 0) {
            sleepTime = dt * 1000.0f - cTime;
        }
//        System.out.println("Sleeping: " + sleepTime);
        try {
            Thread.sleep(Tools.transformFloat(sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time += dt;
    }

    private void countCorrectEn(ASS thing) {
        correctEn += thing.energy.count();
    }

    public void countEn() {
        energy = 1.0;
        for (ASS thing : spheres) {
            energy += thing.energy.count();
        }
    }

    public void fixEnergy() {
        double ratio = correctEn / energy;
        for (ASS thing : spheres) {
            thing.v.mul(Math.sqrt(ratio));
        }
        countEn();
    }


    public double getTime() {
        return time;
    }


    public void addWall(float x1, float y1, float x2, float y2, Material material) {
        Wall wall = new Wall(x1, y1, x2, y2, material);
        walls.add(wall);
        drawables.add(wall);
    }


    public void addWall(float x1, float y1, float x2, float y2) {
        Wall wall = new Wall(x1, y1, x2, y2, Material.Constantin);
        walls.add(wall);
        drawables.add(wall);
    }

    public void addThing(Vector2 v, float w, float x0, float y0, float r) {
        ASS thing = new ASS(this, v, w, x0, y0, r, Material.Constantin);
        spheres.add(thing);
        countCorrectEn(thing);
        drawables.add(thing);
        amOfTh++;
    }

    public void addThing(Vector2 v, float w, float x0, float y0, float r, Material material) {
        ASS sphere = new ASS(this, v, w, x0, y0, r, material);
        spheres.add(sphere);
        countCorrectEn(sphere);
        drawables.add(sphere);
        amOfTh++;
    }

    public void addTriangle(Vector2 v, float w, float x0, float y0, float r, Material material) {
        AST triangle = new AST(this, v, w, x0, y0, r, material);
        triangles.add(triangle);
        drawables.add(triangle);
    }

    public void addTriangle(Vector2 v, float w, float x0, float y0, float r) {
        AST triangle = new AST(this, v, w, x0, y0, r, Material.Constantin);
        triangles.add(triangle);
        drawables.add(triangle);
    }

    public void printEnergy() {
        System.out.printf("Correct energy:\t%.5f\nReal energy:\t%.5f\n", correctEn, energy);
    }

    public void printThings() {
        System.out.printf("Total amount of things:\t%d\n", amOfTh);
    }
}
